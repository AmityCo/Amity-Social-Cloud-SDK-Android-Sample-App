package com.amity.sample.ascsdk.channellist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.sample.ascsdk.BuildConfig
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.channellist.dialog.ChannelInputsDialog
import com.amity.sample.ascsdk.channellist.filter.ChannelQueryFilterActivity
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.common.preferences.SamplePreferences
import com.amity.sample.ascsdk.intent.IntentRequestCode
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.chat.channel.AmityChannelFilter
import com.amity.socialcloud.sdk.core.AmityTags
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.core.file.AmityUploadResult
import com.ekoapp.ekosdk.internal.AmityPagingDataRefresher
import com.google.common.base.Joiner
import com.google.common.collect.FluentIterable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_channel_list.*


class ChannelListActivity : AppCompatActivity(), ChannelInputsDialog.OnDialogListener {

    private var channels: LiveData<PagedList<AmityChannel>>? = null
    private val channelRepository = AmityChatClient.newChannelRepository()
    private var channelType: String = ""

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_channel_list)
        val appName = getString(R.string.app_name)
        toolbar.title = String.format("%s %s: %s", appName, "ASC SDK", BuildConfig.VERSION_NAME)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)

        query_filter_textview.setOnClickListener {
            startActivityForResult(Intent(this, ChannelQueryFilterActivity::class.java),
                    IntentRequestCode.REQUEST_CHANNEL_FILTER_OPTION)
        }

        LiveDataReactiveStreams.fromPublisher(channelRepository.getTotalUnreadCount())
                .observe(this, Observer<Int> { totalUnreadCount ->
                    val totalUnreadCountString = getString(R.string.total_unread_d, totalUnreadCount)
                    total_unread_textview.text = totalUnreadCountString
                })

        observeChannelCollection()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_channel_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create_channel -> {
                val channelTypeItems = ArrayList<String>()
                channelTypeItems.run {
                    add(AmityChannel.Type.COMMUNITY.apiKey)
                    add(AmityChannel.Type.LIVE.apiKey)
                }

                MaterialDialog(this).show {
                    listItems(items = channelTypeItems) { _, _, text ->
                        channelType = text.toString()
                        showChannelInputsDialog(getString(R.string.channel_channelid),
                                getString(R.string.channel_display_name),
                                getString(R.string.channel_user_ids),
                                getString(R.string.cancel),
                                getString(R.string.create))
                    }
                }
                return true
            }
            R.id.action_create_conversation -> {
                channelType = AmityChannel.Type.CONVERSATION.apiKey
                showChannelInputsDialog("",
                        getString(R.string.channel_display_name), getString(R.string.channel_user_id),
                        getString(R.string.cancel),
                        getString(R.string.create))
                return true
            }
            R.id.action_join_channel -> {
                showDialog(R.string.join_channel, "channelId", "", false) { d, input ->
                    val channelId = input.toString()
                    joinChannel(channelId)
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onLeftClick() {
        //TODO Handle when user click on left button
    }

    override fun onRightClick(channelId: String, displayName: String, userId: String, photoFileUri: Uri?) {
        if (channelType.isNotEmpty()) {
            if (photoFileUri != null) {
                progressbar.visibility = View.VISIBLE
                AmityCoreClient.newFileRepository()
                        .uploadImage(photoFileUri)
                        .isFullImage(true)
                        .build()
                        .transfer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { amityImageUpload ->
                            when (amityImageUpload) {
                                is AmityUploadResult.COMPLETE -> {
                                    updateUser(channelType, channelId, displayName, userId, amityImageUpload.getFile())
                                    progressbar.visibility = View.GONE
                                }
                                is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
                                    //TODO Handle when upload photo is error or cancelled
                                    progressbar.visibility = View.GONE
                                }
                                else -> {
                                }
                            }

                        }.doOnError {
                            //TODO Handle when upload photo is error
                            progressbar.visibility = View.GONE
                        }
                        .subscribe()
            } else {
                updateUser(channelType, channelId, displayName, userId, null)
            }
        }
    }

    private fun updateUser(channelType: String, channelId: String,
                           displayName: String, userId: String,
                           profileImage: AmityImage?) {
        when (AmityChannel.Type.enumOf(channelType)) {
            AmityChannel.Type.COMMUNITY -> {
                createCommunityChannel(channelId, displayName, userId, profileImage)
            }
            AmityChannel.Type.LIVE -> {
                createLiveChannel(channelId, displayName, userId, profileImage)
            }
            AmityChannel.Type.CONVERSATION -> {
                createConversationChannel(displayName, userId, profileImage)
            }
            else -> {
            }
        }
    }

    private fun observeChannelCollection() {
        channels?.removeObservers(this)
        val adapter = ChannelListAdapter()
        channel_list_recyclerview.adapter = adapter
        channel_list_recyclerview.addOnScrollListener(AmityPagingDataRefresher())

        displayQueryOptions()
        channels = getChannelsLiveData()
        channels?.observe(this, Observer<PagedList<AmityChannel>> {
            adapter.submitList(it)
        })
    }

    private fun getChannelsLiveData(): LiveData<PagedList<AmityChannel>> {
        val types = FluentIterable.from(SamplePreferences.getChannelTypeOptions().get())
                .transform {
                    AmityChannel.Type.enumOf(it)
                }.toList()

        val filter = AmityChannelFilter.fromApiKey(SamplePreferences.getChannelMembershipOption().get())
        val includingTags = AmityTags(SamplePreferences.getIncludingChannelTags().get())
        val excludingTags = AmityTags(SamplePreferences.getExcludingChannelTags().get())
        val includeDeleted = SamplePreferences.getIncludeDeletedOptions().get()

        return LiveDataReactiveStreams.fromPublisher(channelRepository
                .getChannels()
                .types(types)
                .filter(filter)
                .includingTags(includingTags)
                .excludingTags(excludingTags)
                .includeDeleted(includeDeleted)
                .build()
                .query())
    }

    private fun displayQueryOptions() {
        val types = "Types: " + Joiner.on(",").join(SamplePreferences.getChannelTypeOptions().get())
        val filter = "\nMembership: " + SamplePreferences.getChannelMembershipOption().get()
        val includeTags = "\nincludeTags: " + Joiner.on(",").join(SamplePreferences.getIncludingChannelTags().get())
        val excludingTags = "\nexcludingTags: " + Joiner.on(",").join(SamplePreferences.getExcludingChannelTags().get())
        val includeDeleted = "\nInclude Deleted: " + SamplePreferences.getIncludeDeletedOptions().get()

        query_filter_textview.text = types + filter + includeTags + excludingTags + includeDeleted
    }

    /**
     * shouldn't be empty value when set any method in Builder
     * **/
    private fun createCommunityChannel(channelId: String, displayName: String, userIds: String, profileImage: AmityImage?) {
        val channelCreator = channelRepository.createChannel()
                .communityType()
                .withChannelId(channelId)

        if (displayName.isNotEmpty()) {
            channelCreator.displayName(displayName)
        }
        if (profileImage != null) {
            channelCreator.avatar(profileImage)
        }
        if (userIds.isNotEmpty()) {
            channelCreator.userIds(userIds.split(","))
        }
        channelCreator.build()
                .create()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showToast("Completed")
                }, {
                    showToast(it.message ?: "Failed")
                })
    }

    /**
     * shouldn't be empty value when set any method in Builder
     * **/
    private fun createLiveChannel(channelId: String, displayName: String, userIds: String, profileImage: AmityImage?) {
        val channelCreator = channelRepository.createChannel()
                .liveType()
                .withChannelId(channelId)

        if (displayName.isNotEmpty()) {
            channelCreator.displayName(displayName)
        }
        if (profileImage != null) {
            channelCreator.avatar(profileImage)
        }
        if (userIds.isNotEmpty()) {
            channelCreator.userIds(userIds.split(","))
        }
        channelCreator.build()
                .create()
                .subscribe({
                    showToast("Completed")
                }, {
                    showToast(it.message ?: "Failed")
                })
    }

    /**
     * shouldn't be empty value when set any method in Builder
     * **/
    private fun createConversationChannel(displayName: String, userId: String, profileImage: AmityImage?) {
        val channelCreator = channelRepository.createChannel()
                .conversationType()
                .withUserId(userId)

        if (displayName.isNotEmpty()) {
            channelCreator.displayName(displayName)
        }
        if (profileImage != null) {
            channelCreator.avatar(profileImage)
        }
        channelCreator.build()
                .create()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showToast("Completed")
                }, {
                    showToast(it.message ?: "Failed")
                })
    }

    private fun joinChannel(channelId: String) {
        channelRepository
                .joinChannel(channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IntentRequestCode.REQUEST_CHANNEL_FILTER_OPTION) {
            observeChannelCollection()
        }
    }

    private fun showChannelInputsDialog(channelIdHint: String = "", displayNameHint: String = "",
                                        userIdHint: String = "", textBtnLeft: String = "", textBtnRight: String = "") {
        ChannelInputsDialog.Builder()
                .channelIdHint(channelIdHint)
                .displayNameHint(displayNameHint)
                .userIdHint(userIdHint)
                .textBtnLeftDialog(textBtnLeft)
                .textBtnRightDialog(textBtnRight)
                .build()
                .show(supportFragmentManager, "")
    }
}
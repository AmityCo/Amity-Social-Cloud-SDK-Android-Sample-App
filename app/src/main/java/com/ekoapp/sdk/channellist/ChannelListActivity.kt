package com.ekoapp.sdk.channellist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.ekosdk.channel.EkoChannel
import com.ekoapp.ekosdk.channel.query.EkoChannelFilter
import com.ekoapp.ekosdk.file.EkoImage
import com.ekoapp.ekosdk.file.upload.EkoUploadResult
import com.ekoapp.ekosdk.sdk.BuildConfig
import com.ekoapp.sdk.R
import com.ekoapp.sdk.channellist.dialog.ChannelInputsDialog
import com.ekoapp.sdk.channellist.filter.ChannelQueryFilterActivity
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.common.preferences.SamplePreferences
import com.ekoapp.sdk.intent.IntentRequestCode
import com.google.common.base.Joiner
import com.google.common.collect.FluentIterable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_channel_list.*


class ChannelListActivity : AppCompatActivity(), ChannelInputsDialog.OnDialogListener {

    private var channels: LiveData<PagedList<EkoChannel>>? = null
    private val channelRepository = EkoClient.newChannelRepository()
    private var channelType: String = ""

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_channel_list)
        val appName = getString(R.string.app_name)
        toolbar.title = String.format("%s %s: %s", appName, "Eko SDK", BuildConfig.VERSION_NAME)
        toolbar.subtitle = String.format("%s", baseContext.getString(R.string.sdk_environment))
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
                    add(EkoChannel.Type.COMMUNITY.apiKey)
                    add(EkoChannel.Type.LIVE.apiKey)
                }

                MaterialDialog(this).show {
                    listItems(items = channelTypeItems) { _, _, text ->
                        channelType = text.toString()
                        showChannelInputsDialog(getString(R.string.channel_id),
                                getString(R.string.display_name),
                                getString(R.string.cancel),
                                getString(R.string.create))
                    }
                }
                return true
            }
            R.id.action_create_conversation -> {
                channelType = EkoChannel.Type.CONVERSATION.apiKey
                showChannelInputsDialog(getString(R.string.user_id),
                        getString(R.string.display_name),
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

    override fun onRightClick(firstInput: String, secondInput: String, photoFileUri: Uri?) {
        if (channelType.isNotEmpty()) {
            if (photoFileUri != null) {
                EkoClient.newFileRepository()
                        .uploadImage(photoFileUri)
                        .isFullImage(true)
                        .build()
                        .transfer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { ekoImageUpload ->
                            when (ekoImageUpload) {
                                is EkoUploadResult.COMPLETE -> {
                                    updateUser(channelType, firstInput, secondInput, ekoImageUpload.getFile())
                                }
                                is EkoUploadResult.ERROR, EkoUploadResult.CANCELLED -> {
                                    //TODO Handle when upload photo is error or cancelled
                                }
                                else -> {
                                }
                            }
                        }.doOnError {
                            //TODO Handle when upload photo is error
                        }
                        .subscribe()
            } else {
                updateUser(channelType, firstInput, secondInput, null)
            }
        }
    }

    private fun updateUser(channelType: String, firstInput: String,
                           secondInput: String, profileImage: EkoImage?) {
        when (EkoChannel.Type.enumOf(channelType)) {
            EkoChannel.Type.COMMUNITY -> {
                createCommunityChannel(firstInput, secondInput, profileImage)
            }
            EkoChannel.Type.LIVE -> {
                createLiveChannel(firstInput, secondInput, profileImage)
            }
            EkoChannel.Type.CONVERSATION -> {
                createConversationChannel(firstInput, secondInput, profileImage)
            }
            else -> {
            }
        }
    }

    private fun observeChannelCollection() {
        channels?.removeObservers(this)
        val adapter = ChannelListAdapter()
        channel_list_recyclerview.adapter = adapter

        displayQueryOptions()
        channels = getChannelsLiveData()
        channels?.observe(this, Observer<PagedList<EkoChannel>> {
            adapter.submitList(it)
        })
    }

    private fun getChannelsLiveData(): LiveData<PagedList<EkoChannel>> {
        val types = FluentIterable.from(SamplePreferences.getChannelTypeOptions().get())
                .transform {
                    EkoChannel.Type.enumOf(it)
                }.toList()

        val filter = EkoChannelFilter.fromApiKey(SamplePreferences.getChannelMembershipOption().get())
        val includingTags = EkoTags(SamplePreferences.getIncludingChannelTags().get())
        val excludingTags = EkoTags(SamplePreferences.getExcludingChannelTags().get())
        val includeDeleted = SamplePreferences.getIncludeDeletedOptions().get()

        return LiveDataReactiveStreams.fromPublisher(channelRepository
                .getChannelCollection()
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
    private fun createCommunityChannel(channelId: String, displayName: String, profileImage: EkoImage?) {
        val channelCreator = channelRepository.createChannel()
                .communityType()
                .withChannelId(channelId)

        if (displayName.isNotEmpty()) {
            channelCreator.displayName(displayName)
        }
        if (profileImage != null) {
            channelCreator.avatar(profileImage)
        }
        channelCreator.build()
                .create()
                .subscribe()
    }

    /**
     * shouldn't be empty value when set any method in Builder
     * **/
    private fun createLiveChannel(channelId: String, displayName: String, profileImage: EkoImage?) {
        val channelCreator = channelRepository.createChannel()
                .liveType()
                .withChannelId(channelId)

        if (displayName.isNotEmpty()) {
            channelCreator.displayName(displayName)
        }
        if (profileImage != null) {
            channelCreator.avatar(profileImage)
        }
        channelCreator.build()
                .create()
                .subscribe()
    }

    /**
     * shouldn't be empty value when set any method in Builder
     * **/
    private fun createConversationChannel(userId: String, displayName: String, profileImage: EkoImage?) {
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
                .doOnSuccess {
                    Toast.makeText(this,
                            String.format("conversation created with %s", userId),
                            Toast.LENGTH_SHORT).show()
                }
                .subscribe()
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

    private fun showChannelInputsDialog(firstInputHint: String, secondInputHint: String,
                                        textBtnLeft: String, textBtnRight: String) {
        ChannelInputsDialog.Builder()
                .firstInputHint(firstInputHint)
                .secondInputHint(secondInputHint)
                .textBtnLeftDialog(textBtnLeft)
                .textBtnRightDialog(textBtnRight)
                .build()
                .show(supportFragmentManager, "")
    }
}
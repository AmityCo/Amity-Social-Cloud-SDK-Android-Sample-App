package com.ekoapp.simplechat.channellist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.ekosdk.channel.EkoChannel
import com.ekoapp.ekosdk.channel.query.EkoChannelFilter
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.SimplePreferences
import com.ekoapp.simplechat.channellist.filter.ChannelQueryFilterActivity
import com.ekoapp.simplechat.intent.IntentRequestCode
import com.google.common.base.Joiner
import com.google.common.collect.FluentIterable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_channel_list.*


class ChannelListActivity : AppCompatActivity() {

    private val channelRepository = EkoClient.newChannelRepository()
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_channel_list)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)

        query_filter_textview.setOnClickListener {
            startActivityForResult(Intent(this, ChannelQueryFilterActivity::class.java),
                    IntentRequestCode.REQUEST_CHANNEL_FILTER_OPTION)
        }

        observeChannelCollection()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_channel_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create_channel -> {
                val channelTypeItems = listOf(EkoChannel.Type.COMMUNITY.apiKey, EkoChannel.Type.LIVE.apiKey)
                MaterialDialog(this).show {
                    listItems(items = channelTypeItems) { _, _, text ->
                        showDialog(R.string.create_channel, "channelId", "", false) { _, input ->
                            val channelId = input.toString()
                            when (EkoChannel.Type.enumOf(text.toString())) {
                                EkoChannel.Type.COMMUNITY -> {
                                    createCommunityChannel(channelId)
                                }
                                EkoChannel.Type.LIVE -> {
                                    createLiveChannel(channelId)
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }
                return true
            }
            R.id.action_create_conversation -> {
                showDialog(R.string.create_conversation, "userId", "", false) { _, input ->
                    val userId = input.toString()
                    createConversationChannel(userId)
                }
                return true
            }
            R.id.action_join_channel -> {
                showDialog(R.string.join_channel, "channelId", "", false) { _, input ->
                    val channelId = input.toString()
                    channelRepository.joinChannel(channelId).subscribe()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun observeChannelCollection() {
        val adapter = ChannelListAdapter()
        channel_list_recyclerview.adapter = adapter
        displayQueryOptions()

        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(getChannels()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.submitList(it)
                }, {})
        )
    }

    private fun getChannels(): Flowable<PagedList<EkoChannel>> {
        val types = FluentIterable.from(SimplePreferences.getChannelTypeOptions().get())
                .transform {
                    EkoChannel.Type.enumOf(it)
                }.toList()

        val filter = EkoChannelFilter.fromApiKey(SimplePreferences.getChannelMembershipOption().get())
        val includingTags = EkoTags(SimplePreferences.getIncludingChannelTags().get())
        val excludingTags = EkoTags(SimplePreferences.getExcludingChannelTags().get())

        return channelRepository
                .getChannelCollection()
                .types(types)
                .filter(filter)
                .includingTags(includingTags)
                .excludingTags(excludingTags)
                .build()
                .query()
    }

    private fun displayQueryOptions() {
        val types = "Types: " + Joiner.on(",").join(SimplePreferences.getChannelTypeOptions().get())
        val filter = "\nMembership: " + SimplePreferences.getChannelMembershipOption().get()
        val includeTags = "\nincludeTags: " + Joiner.on(",").join(SimplePreferences.getIncludingChannelTags().get())
        val excludingTags = "\nexcludingTags: " + Joiner.on(",").join(SimplePreferences.getExcludingChannelTags().get())
        val queryFilter = "$types $filter $includeTags $excludingTags"
        query_filter_textview.text = queryFilter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IntentRequestCode.REQUEST_CHANNEL_FILTER_OPTION) {
            observeChannelCollection()
        }
    }

    private fun createCommunityChannel(channelId: String) {
        channelRepository.createChannel()
                .communityType()
                .withChannelId(channelId)
                .displayName(channelId)
                .build()
                .create()
                .subscribe()
    }

    private fun createLiveChannel(channelId: String) {
        channelRepository.createChannel()
                .liveType()
                .withChannelId(channelId)
                .displayName(channelId)
                .build()
                .create()
                .subscribe()
    }

    private fun createConversationChannel(userId: String) {
        channelRepository.createChannel()
                .conversationType()
                .withUserIds(listOf(userId))
                .build()
                .create()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Toast.makeText(this,
                            String.format("conversation created with %s", userId),
                            Toast.LENGTH_SHORT).show()
                }
                .subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}
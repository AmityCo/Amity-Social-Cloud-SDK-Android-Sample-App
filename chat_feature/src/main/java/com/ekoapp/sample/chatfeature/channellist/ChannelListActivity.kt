package com.ekoapp.sample.chatfeature.channellist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channellist.filter.ChannelQueryFilterActivity
import com.ekoapp.sample.core.intent.IntentRequestCode
import com.ekoapp.sample.core.preferences.SimplePreferences
import com.ekoapp.sample.core.base.BaseActivity
import com.google.common.collect.FluentIterable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_channel_list.*
import java.util.*


class ChannelListActivity : BaseActivity() {

    private var channels: LiveData<PagedList<EkoChannel>>? = null

    private val channelRepository = EkoClient.newChannelRepository()


    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_channel_list)
        toolbar.title = getString(R.string.toolbar_channel_list)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)

        LiveDataReactiveStreams.fromPublisher(channelRepository.getTotalUnreadCount())
                .observe(this, Observer<Int> { totalUnreadCount ->
                    val totalUnreadCountString = getString(R.string.total_unread_d, totalUnreadCount)
                    toolbar.subtitle = totalUnreadCountString
                    toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
                })

        observeChannelCollection()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_channel_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_filter_channel) {
            startActivityForResult(Intent(this, ChannelQueryFilterActivity::class.java),
                    IntentRequestCode.REQUEST_CHANNEL_FILTER_OPTION)
            return true
        } else if (id == R.id.action_create_channel) {
            val channelTypeItems = ArrayList<String>()
            channelTypeItems.run {
                add(EkoChannel.CreationType.STANDARD.apiKey)
                add(EkoChannel.CreationType.PRIVATE.apiKey)
            }

            MaterialDialog(this).show {
                listItems(items = channelTypeItems) { dialog, index, text ->
                    showDialog(R.string.create_channel, "channelId", "", false, { d, input ->
                        val channelId = input.toString()
                        channelRepository.createChannel(channelId,
                                EkoChannel.CreationType.fromJson(text.toString()),
                                EkoChannel.CreateOption.none())
                    })
                }
            }
            return true
        } else if (id == R.id.action_create_conversation) {
            showDialog(R.string.create_conversation, "userId", "", false, { d, input ->
                val userId = input.toString()
                channelRepository.createConversation(userId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { Toast.makeText(this, String.format("conversation created with %s", userId), Toast.LENGTH_SHORT).show() }
                        .subscribe()
            })
            return true
        } else if (id == R.id.action_join_channel) {
            showDialog(R.string.join_channel, "channelId", "", false, { d, input ->
                val channelId = input.toString()
                channelRepository.joinChannel(channelId).subscribe()
            })
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeChannelCollection() {
        channels?.removeObservers(this)
        val adapter = ChannelListAdapter()
        channel_list_recyclerview.adapter = adapter

        channels = getChannelsLiveData()
        channels?.observe(this, Observer { adapter.submitList(it) })
    }

    private fun getChannelsLiveData(): LiveData<PagedList<EkoChannel>> {

        val types = FluentIterable.from(SimplePreferences.getChannelTypeOptions().get())
                .transform {
                    EkoChannel.Type.fromJson(it)
                }.toSet()

        val filter = EkoChannelFilter.fromApiKey(SimplePreferences.getChannelMembershipOption().get())
        val includingTags = EkoTags(SimplePreferences.getIncludingChannelTags().get())
        val excludingTags = EkoTags(SimplePreferences.getExcludingChannelTags().get())

        return channelRepository
                .channelCollection
                .byTypes()
                .types(types)
                .filter(filter)
                .includingTags(includingTags)
                .excludingTags(excludingTags)
                .build()
                .query()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IntentRequestCode.REQUEST_CHANNEL_FILTER_OPTION) {
            observeChannelCollection()
        }
    }

    private fun showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
        MaterialDialog(this).show {
            title(title)
            input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
        }
    }

}
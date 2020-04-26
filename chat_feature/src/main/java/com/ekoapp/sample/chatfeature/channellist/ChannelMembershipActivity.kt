package com.ekoapp.sample.chatfeature.channellist

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoChannelMembership
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.intent.ViewChannelMembershipsIntent
import com.ekoapp.sample.core.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_channel_membership_list.*

class ChannelMembershipActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_membership_list)
        val channelId = ViewChannelMembershipsIntent.getChannelId(intent)
        toolbar.title = channelId
        toolbar.subtitle = "member"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
        val adapter = ChannelMembershipAdapter()
        channel_membership_list_recyclerview.adapter = adapter

        EkoClient.newChannelRepository()
                .membership(channelId)
                .collection
                .observe(this, Observer { pagedList: PagedList<EkoChannelMembership?> -> adapter.submitList(pagedList) })
    }
}

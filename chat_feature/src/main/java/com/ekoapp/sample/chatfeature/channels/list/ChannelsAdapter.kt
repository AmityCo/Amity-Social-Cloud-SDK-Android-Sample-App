package com.ekoapp.sample.chatfeature.channels.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoChannelAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channels.ChannelsViewModel
import com.ekoapp.sample.chatfeature.channels.list.viewholder.ChannelsViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder

class ChannelsAdapter(private val context: Context, private val viewModel: ChannelsViewModel) : EkoChannelAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false)
        return ChannelsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ChannelsViewHolder -> {
                getItem(position)?.apply {
                    holder.bind(this)
                    holder.joinChannel { viewModel.bindJoinChannel(this.channelId) }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}


package com.ekoapp.sample.chatfeature.channels.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoChannelAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channels.ChannelsViewModel
import com.ekoapp.sample.chatfeature.channels.list.viewholder.ChannelViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder

class MainChannelsAdapter(private val context: Context, private val viewModel: ChannelsViewModel) : EkoChannelAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ChannelViewHolder -> {
                getItem(position)?.apply {
                    holder.bind(this)
                    holder.joinChannel { viewModel.bindJoinChannel(this.channelId) }
                    holder.aboutChannel(context, viewModel.getAboutContent(this))
                    holder.itemView.setOnClickListener {
                        //TODO Check isJoined from server if api available
                        viewModel.bindJoinChannel(this.channelId)
                    }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}


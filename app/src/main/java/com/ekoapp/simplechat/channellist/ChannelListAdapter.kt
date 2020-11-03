package com.ekoapp.simplechat.channellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoChannelAdapter
import com.ekoapp.ekosdk.channel.EkoChannel
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.intent.ViewParentMessagesIntent
import com.google.common.base.Joiner
import kotlinx.android.synthetic.main.item_channel.view.*

class ChannelListAdapter : EkoChannelAdapter<ChannelListAdapter.ChannelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel = getItem(position)
        if (channel == null) {
            holder.channel = null
            holder.itemView.channel_textview.text = "loading..."
        } else {
            val text = StringBuilder()
                    .append("id: ")
                    .append(channel.getChannelId())
                    .append("\nname: ")
                    .append(channel.getDisplayName())
                    .append("\nmember count: ")
                    .append(channel.getMemberCount())
                    .append("\nunread count: ")
                    .append(channel.getUnreadCount())
                    .append("\nmessage count: ")
                    .append(channel.getMessageCount())
                    .append("\ntags: ")
                    .append(Joiner.on(", ").join(channel.getTags()))
                    .append("\ntype: ")
                    .append(channel.getType().apiKey)
                    .toString()
            holder.channel = channel
            holder.itemView.channel_textview.text = text
        }
    }

    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var channel: EkoChannel? = null

        init {
            itemView.setOnClickListener { view: View ->
                channel?.let {
                    val context = view.context
                    val intent = ViewParentMessagesIntent(context, it)
                    context.startActivity(intent)
                }
            }
        }
    }

}
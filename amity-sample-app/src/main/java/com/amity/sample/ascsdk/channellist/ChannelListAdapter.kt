package com.amity.sample.ascsdk.channellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.extension.adapter.AmityChannelAdapter
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.ViewParentMessagesIntent
import com.google.common.base.Joiner
import kotlinx.android.synthetic.main.item_channel.view.*

class ChannelListAdapter : AmityChannelAdapter<ChannelListAdapter.ChannelViewHolder>() {
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
                    .append("\ndeleted: ")
                    .append(channel.isDeleted())
                    .toString()
            holder.channel = channel
            holder.itemView.channel_textview.text = text
        }
    }

    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var channel: AmityChannel? = null

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

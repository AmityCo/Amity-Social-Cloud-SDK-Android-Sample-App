package com.ekoapp.sample.chatfeature.channellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.EkoObjects
import com.ekoapp.ekosdk.adapter.EkoChannelAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channellist.ChannelListAdapter.ChannelViewHolder
import com.ekoapp.sample.chatfeature.intent.ViewParentMessagesIntent
import com.google.common.base.Joiner
import kotlinx.android.synthetic.main.item_channel.view.*

class ChannelListAdapter : EkoChannelAdapter<ChannelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel = getItem(position)
        if (EkoObjects.isProxy(channel)) {
            holder.channelId = null
            holder.itemView.channel_textview.text = "loading..."
        } else {
            val text = StringBuilder()
                    .append("id: ")
                    .append(channel!!.channelId)
                    .append("\nname: ")
                    .append(channel.displayName)
                    .append("\nmember count: ")
                    .append(channel.memberCount)
                    .append("\nunread count: ")
                    .append(channel.unreadCount)
                    .append("\nmessage count: ")
                    .append(channel.messageCount)
                    .append("\ntags: ")
                    .append(Joiner.on(", ").join(channel.tags))
                    .append("\ntype: ")
                    .append(channel.channelType)
                    .toString()
            holder.channelId = channel.channelId
            holder.itemView.channel_textview.text = text
        }
    }

    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var channelId: String? = null

        init {
            itemView.setOnClickListener { view: View ->
                val context = view.context
                val intent = ViewParentMessagesIntent(context, channelId!!)
                context.startActivity(intent)
            }
        }
    }
}

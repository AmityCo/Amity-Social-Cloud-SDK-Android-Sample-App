package com.ekoapp.sample.chatfeature.channellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.EkoObjects
import com.ekoapp.ekosdk.adapter.EkoChannelMembershipAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channellist.ChannelMembershipAdapter.EkoUserViewHolder
import kotlinx.android.synthetic.main.item_channel_membership.view.*

class ChannelMembershipAdapter : EkoChannelMembershipAdapter<EkoUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EkoUserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_channel_membership, parent, false)
        return EkoUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: EkoUserViewHolder, position: Int) {
        val channelMembership = getItem(position)
        if (EkoObjects.isProxy(channelMembership)) {
            holder.itemView.channel_membership_textview.text = "loading..."
        } else {
            holder.itemView.channel_membership_textview.text = StringBuilder()
                    .append(position + 1)
                    .append("\nid: ")
                    .append(channelMembership!!.userId)
                    .append("\ndisplay name: ")
                    .append(if (channelMembership.user != null) channelMembership.user!!.displayName else "null")
                    .append("\nmembership: ")
                    .append(channelMembership.membership)
                    .append("\nis muted: ")
                    .append(channelMembership.isMuted)
                    .append("\nis banned: ")
                    .append(channelMembership.isBanned)
                    .toString()
        }
    }

    class EkoUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

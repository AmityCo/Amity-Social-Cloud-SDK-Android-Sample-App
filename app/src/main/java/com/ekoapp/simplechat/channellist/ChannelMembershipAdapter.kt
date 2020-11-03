package com.ekoapp.simplechat.channellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoChannelMembershipAdapter
import com.ekoapp.simplechat.R
import kotlinx.android.synthetic.main.item_channel_membership.view.*

class ChannelMembershipAdapter : EkoChannelMembershipAdapter<ChannelMembershipAdapter.EkoUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EkoUserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_channel_membership, parent, false)
        return EkoUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: EkoUserViewHolder, position: Int) {
        val channelMembership = getItem(position)
        if (channelMembership == null) {
            holder.itemView.channel_membership_textview.text = "loading..."
        } else {
            holder.itemView.channel_membership_textview.text = StringBuilder()
                    .append(position + 1)
                    .append("\nid: ")
                    .append(channelMembership?.getUserId())
                    .append("\ndisplay name: ")
                    .append(channelMembership.getUser()?.getDisplayName() ?: "null")
                    .append("\nmembership: ")
                    .append(channelMembership.getMembership())
                    .append("\nis muted: ")
                    .append(channelMembership.isMuted())
                    .append("\nis banned: ")
                    .append(channelMembership.isBanned())
                    .append("\nis role: ")
                    .append(channelMembership.getRoles())
                    .toString()
        }
    }

    class EkoUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
package com.amity.sample.ascsdk.channellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.chat.channel.AmityChannelMember
import com.amity.socialcloud.sdk.extension.adapter.AmityChannelMembershipAdapter
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_channel_membership.view.*

class ChannelMembershipAdapter : AmityChannelMembershipAdapter<ChannelMembershipAdapter.AmityUserViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<AmityChannelMember>()
    private val onClickSubject = PublishSubject.create<AmityChannelMember>()

    val onLongClickFlowable: Flowable<AmityChannelMember>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<AmityChannelMember>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityUserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_channel_membership, parent, false)
        return AmityUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmityUserViewHolder, position: Int) {
        val channelMembership = getItem(position)
        if (channelMembership == null) {
            holder.itemView.channel_membership_textview.text = "loading..."
        } else {
            holder.itemView.channel_membership_textview.text = StringBuilder()
                    .append(position + 1)
                    .append("\nid: ")
                    .append(channelMembership.getUserId())
                    .append("\ndisplay name: ")
                    .append(channelMembership.getUser()?.getDisplayName() ?: "null")
                    .append("\nis muted: ")
                    .append(channelMembership.isMuted())
                    .append("\nis banned: ")
                    .append(channelMembership.isBanned())
                    .append("\nrole: ")
                    .append(channelMembership.getRoles())
                    .toString()

            holder.itemView.setOnClickListener {
                onClickSubject.onNext(channelMembership)
            }

            holder.itemView.setOnLongClickListener {
                onLongClickSubject.onNext(channelMembership)
                true
            }
        }
    }

    class AmityUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

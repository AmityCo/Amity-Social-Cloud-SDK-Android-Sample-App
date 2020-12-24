package com.ekoapp.sdk.channellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoChannelMembershipAdapter
import com.ekoapp.ekosdk.channel.membership.EkoChannelMembership
import com.ekoapp.sdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_channel_membership.view.*

class ChannelMembershipAdapter : EkoChannelMembershipAdapter<ChannelMembershipAdapter.EkoUserViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<EkoChannelMembership>()
    private val onClickSubject = PublishSubject.create<EkoChannelMembership>()

    val onLongClickFlowable: Flowable<EkoChannelMembership>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<EkoChannelMembership>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

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
                    .append(channelMembership.getUserId())
                    .append("\ndisplay name: ")
                    .append(channelMembership.getUser()?.getDisplayName() ?: "null")
                    .append("\nmembership: ")
                    .append(channelMembership.getMembership())
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

    class EkoUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

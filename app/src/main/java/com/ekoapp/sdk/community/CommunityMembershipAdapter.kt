package com.ekoapp.sdk.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoCommunityMembershipAdapter
import com.ekoapp.ekosdk.community.membership.EkoCommunityMembership
import com.ekoapp.sdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_community_membership.view.*

class CommunityMembershipAdapter : EkoCommunityMembershipAdapter<CommunityMembershipAdapter.EkoUserViewHolder>() {
    private val onLongClickSubject = PublishSubject.create<EkoCommunityMembership>()
    private val onClickSubject = PublishSubject.create<EkoCommunityMembership>()

    val onLongClickFlowable: Flowable<EkoCommunityMembership>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<EkoCommunityMembership>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EkoUserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_community_membership, parent, false)
        return EkoUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: EkoUserViewHolder, position: Int) {
        val communityMembership = getItem(position)
        if (communityMembership == null) {
            holder.itemView.community_membership_textview.text = "loading..."
        } else {
            holder.itemView.community_membership_textview.text = StringBuilder()
                    .append(position + 1)
                    .append("\nid: ")
                    .append(communityMembership.getUserId())
                    .append("\ndisplay name: ")
                    .append(communityMembership.getUser()?.getDisplayName())
                    .append("\nchannel id: ")
                    .append(communityMembership.getChannelId())
                    .append("\ncommunity id: ")
                    .append(communityMembership.getCommunityId())
                    .append("\nmembership: ")
                    .append(communityMembership.getCommunityMembership())
                    .append("\nis banned: ")
                    .append(communityMembership.isBanned())
                    .append("\nrole: ")
                    .append(communityMembership.getRoles())
                    .toString()

            holder.itemView.setOnClickListener {
                onClickSubject.onNext(communityMembership)
            }

            holder.itemView.setOnLongClickListener {
                onLongClickSubject.onNext(communityMembership)
                true
            }
        }
    }

    class EkoUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}

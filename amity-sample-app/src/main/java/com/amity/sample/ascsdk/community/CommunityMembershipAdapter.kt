package com.amity.sample.ascsdk.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.extension.adapter.AmityCommunityMembershipAdapter
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_community_membership.view.*

class CommunityMembershipAdapter : AmityCommunityMembershipAdapter<CommunityMembershipAdapter.AmityUserViewHolder>() {
    private val onLongClickSubject = PublishSubject.create<AmityCommunityMember>()
    private val onClickSubject = PublishSubject.create<AmityCommunityMember>()

    val onLongClickFlowable: Flowable<AmityCommunityMember>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<AmityCommunityMember>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityUserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_community_membership, parent, false)
        return AmityUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmityUserViewHolder, position: Int) {
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

    class AmityUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}

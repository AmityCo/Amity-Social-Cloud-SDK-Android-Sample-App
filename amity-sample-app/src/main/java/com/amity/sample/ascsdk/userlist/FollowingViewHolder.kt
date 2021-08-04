package com.amity.sample.ascsdk.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import com.google.common.base.Objects
import kotlinx.android.synthetic.main.item_user_follow.view.*

class FollowingViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_user_follow, parent, false)) {
    private var postTextView: TextView? = null

    init {
        postTextView = itemView.findViewById(R.id.user_follow_textview)
    }

    fun bind(follow: AmityFollowRelationship) {
        val text = StringBuilder()
                .append("userId: ")
                .append(follow.getTargetUser()?.getUserId())
                .append("\ndisplay name: ")
                .append(follow.getTargetUser()?.getDisplayName())
                .append("\nfollow status: ")
                .append(follow.getStatus().apiKey)
                .toString()

        itemView.user_follow_textview.text = text
    }

    object FollowingDiffUtil : DiffUtil.ItemCallback<AmityFollowRelationship>() {
        override fun areItemsTheSame(oldItem: AmityFollowRelationship, newItem: AmityFollowRelationship): Boolean {
            return Objects.equal(oldItem.getTargetUser()?.getUserId(), newItem.getTargetUser()?.getUserId())
        }

        override fun areContentsTheSame(oldItem: AmityFollowRelationship, newItem: AmityFollowRelationship): Boolean {
            return Objects.equal(oldItem.getSourceUser()?.getUserId(), newItem.getSourceUser()?.getUserId())
                    && Objects.equal(oldItem.getTargetUser()?.getUserId(), newItem.getTargetUser()?.getUserId())
                    && Objects.equal(oldItem.getStatus().apiKey, newItem.getStatus().apiKey)
        }
    }
}
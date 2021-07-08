package com.amity.sample.ascsdk.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import com.google.common.base.Objects
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_user_follow.view.*

class UserFollowingListAdapter : PagedListAdapter<AmityFollowRelationship, UserFollowingListAdapter.UserFollowListViewHolder>(DIFF_CALLBACK) {

    private val onLongClickSubject = PublishSubject.create<AmityFollowRelationship>()
    private val onClickSubject = PublishSubject.create<AmityFollowRelationship>()

    val onLongClickFlowable: Flowable<AmityFollowRelationship>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<AmityFollowRelationship>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user_follow, parent, false)
        return UserFollowListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserFollowListViewHolder, position: Int) {
        val followStatus = getItem(position)

        if (followStatus == null) {
            holder.itemView.user_follow_textview.text = "loading..."
        } else {
            val text = StringBuilder()
                    .append("userId: ")
                    .append(followStatus.getTargetUser()?.getUserId())
                    .append("\ndisplay name: ")
                    .append(followStatus.getTargetUser()?.getDisplayName())
                    .append("\nfollow status: ")
                    .append(followStatus.getStatus().apiKey)
                    .toString()
            holder.itemView.user_follow_textview.text = text

            holder.itemView.setOnClickListener {
                onClickSubject.onNext(followStatus)
            }

            holder.itemView.setOnLongClickListener {
                onLongClickSubject.onNext(followStatus)
                true
            }
        }
    }

    class UserFollowListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

private val DIFF_CALLBACK: DiffUtil.ItemCallback<AmityFollowRelationship> = object : DiffUtil.ItemCallback<AmityFollowRelationship>() {
    override fun areItemsTheSame(oldItem: AmityFollowRelationship, newItem: AmityFollowRelationship): Boolean {
        return Objects.equal(oldItem.getTargetUser()?.getUserId(), newItem.getTargetUser()?.getUserId())
    }

    override fun areContentsTheSame(oldItem: AmityFollowRelationship, newItem: AmityFollowRelationship): Boolean {
        return Objects.equal(oldItem.getSourceUser()?.getUserId(), newItem.getSourceUser()?.getUserId())
                && Objects.equal(oldItem.getTargetUser()?.getUserId(), newItem.getTargetUser()?.getUserId())
                && Objects.equal(oldItem.getStatus().apiKey, newItem.getStatus().apiKey)
    }
}

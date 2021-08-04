package com.amity.sample.ascsdk.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class FollowingPagingDataAdapter : PagingDataAdapter<AmityFollowRelationship,
        FollowingViewHolder>(FollowingViewHolder.FollowingDiffUtil) {

    private val onLongClickSubject = PublishSubject.create<AmityFollowRelationship>()
    private val onClickSubject = PublishSubject.create<AmityFollowRelationship>()

    val onLongClickFlowable: Flowable<AmityFollowRelationship>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<AmityFollowRelationship>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FollowingViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val follow = getItem(position)

        follow?.let { followNonNull ->
            holder.bind(followNonNull)
            holder.itemView.setBackgroundResource(R.color.color_quite_grey)

            holder.itemView.setOnClickListener {
                onClickSubject.onNext(followNonNull)
            }

            holder.itemView.setOnLongClickListener {
                onLongClickSubject.onNext(followNonNull)
                true
            }
        }
    }
}

package com.amity.sample.ascsdk.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.social.feed.AmityPost
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class PostPagingDataAdapter : PagingDataAdapter<AmityPost, PostViewHolder>(
    diffCallback = PostViewHolder.PostDiffUtil
) {

    private val onLongClickSubject = PublishSubject.create<AmityPost>()
    private val onClickSubject = PublishSubject.create<AmityPost>()

    val onLongClickFlowable: Flowable<AmityPost>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<AmityPost>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)

        post?.let { postNonNull ->
            holder.bind(postNonNull)

            holder.itemView.setOnClickListener {
                onClickSubject.onNext(postNonNull)
            }

            holder.itemView.setOnLongClickListener {
                onLongClickSubject.onNext(postNonNull)
                true
            }
        }
    }

}

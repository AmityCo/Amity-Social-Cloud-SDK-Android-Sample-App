package com.amity.sample.ascsdk.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.social.feed.AmityPost
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_post.view.*


class PostListAdapter : PagedListAdapter<AmityPost, PostListAdapter.PostViewHolder>(diffCallback = object : DiffUtil.ItemCallback<AmityPost>() {
    override fun areItemsTheSame(oldItem: AmityPost, newItem: AmityPost): Boolean {
        return oldItem.getPostId() == newItem.getPostId()
    }

    override fun areContentsTheSame(oldItem: AmityPost, newItem: AmityPost): Boolean {
        return oldItem.getPostId() == newItem.getPostId() && oldItem.getUpdatedAt() == newItem.getUpdatedAt()
    }
}) {

    private val onLongClickSubject = PublishSubject.create<AmityPost>()
    private val onClickSubject = PublishSubject.create<AmityPost>()

    val onLongClickFlowable: Flowable<AmityPost>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<AmityPost>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)!!

        post.let {
            val postData = it.getData()
            if (postData is AmityPost.Data.IMAGE || postData is AmityPost.Data.FILE) {
                holder.itemView.post_textview.text = "incorrect type"
                return
            }
            var rootPost = "postId: %s\n" +
                    "target: %s\n" +
                    "feedType: %s\n" +
                    "dataType: %s\n" +
                    "data: %s\n" +
                    "myReactions: %s\n" +
                    "reactionCount: %s\n" +
                    "commentCount: %s\n" +
                    "childrenCount: %s\n" +
                    "flagCount: %s\n" +
                    "isFlaggedByMe: %s\n" +
                    "deleted: %s\n" +
                    "updatedAt: %s\n" +
                    "createdAt: %s"

            val childPost = "\n----------------------------\n" +
                    "   postId: %s\n" +
                    "   parentId: %s\n" +
                    "   target: %s\n" +
                    "   feedType: %s\n" +
                    "   dataType: %s\n" +
                    "   data: %s\n" +
                    "   myReactions: %s\n" +
                    "   reactionCount: %s\n" +
                    "   commentCount: %s\n" +
                    "   flagCount: %s\n" +
                    "   isFlaggedByMe: %s\n" +
                    "   deleted: %s\n" +
                    "   updatedAt: %s\n" +
                    "   createdAt: %s\n"

            var displayPost = String.format(rootPost,
                    post.getPostId(),
                    if (post.getTarget() is AmityPost.Target.COMMUNITY) "community " + (post.getTarget() as AmityPost.Target.COMMUNITY).getCommunity()?.getCommunityId() else "user " + (post.getTarget() as AmityPost.Target.USER).getUser()?.getUserId(),
                    post.getFeedType().apiKey,
                    post.getDataType().apiKey,
                    (post.getData() as? AmityPost.Data.TEXT)?.getText()
                            ?: (post.getData() as? AmityPost.Data.IMAGE)?.getImage()?.getUrl()
                            ?: (post.getData() as? AmityPost.Data.FILE)?.getFile()?.getUrl()
                            ?: (post.getData() as? AmityPost.Data.CUSTOM)?.getRawData()?.toString()
                            ?: (post.getData() as? AmityPost.Data.VIDEO)?.getThumbnailImage()?.getUrl()
                            ?: "unknown",
                    post.getMyReactions(),
                    post.getReactionCount(),
                    post.getCommentCount(),
                    post.getChildren().size,
                    post.getFlagCount(),
                    post.isFlaggedByMe,
                    post.isDeleted(),
                    post.getUpdatedAt(),
                    post.getCreatedAt())

            post.getChildren().forEach {
                displayPost += String.format(childPost,
                        it.getPostId(),
                        it.getParentPostId(),
                        if (it.getTarget() is AmityPost.Target.COMMUNITY) "community " + (it.getTarget() as AmityPost.Target.COMMUNITY).getCommunity()?.getCommunityId() else "user " + (it.getTarget() as AmityPost.Target.USER).getUser()?.getUserId(),
                        it.getFeedType().apiKey,
                        it.getDataType().apiKey,
                        (it.getData() as? AmityPost.Data.TEXT)?.getText(),
                        it.getMyReactions(),
                        it.getReactionCount(),
                        it.getCommentCount(),
                        it.getFlagCount(),
                        it.isFlaggedByMe,
                        it.isDeleted(),
                        it.getUpdatedAt(),
                        it.getCreatedAt())
            }

            holder.itemView.post_textview.text = displayPost
            holder.itemView.setOnClickListener {
                onClickSubject.onNext(post)
            }

            holder.itemView.setOnLongClickListener {
                onLongClickSubject.onNext(post)
                true
            }
        }

    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}

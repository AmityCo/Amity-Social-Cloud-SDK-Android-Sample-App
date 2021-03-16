package com.ekoapp.sdk.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoPostAdapter
import com.ekoapp.ekosdk.feed.EkoPost
import com.ekoapp.sdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_post.view.*


class PostListAdapter : EkoPostAdapter<PostListAdapter.PostViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<EkoPost>()
    private val onClickSubject = PublishSubject.create<EkoPost>()

    val onLongClickFlowable: Flowable<EkoPost>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<EkoPost>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        if (post == null) {
            holder.itemView.post_textview.text = "loading..."
        } else {
            post.let {

                val postData = it.getData()
                if (postData is EkoPost.Data.IMAGE || postData is EkoPost.Data.FILE) {
                    holder.itemView.post_textview.text = "incorrect type"
                    return
                }
                holder.itemView.sender_textview.text = String.format("userId: %s", post.getPostedUserId())
                if (postData is EkoPost.Data.TEXT) {
                    holder.itemView.post_textview.text = String.format("postId: %s\npostDataType: %s", post.getPostId(), "text")
                    holder.itemView.data_textview.text = String.format("text: %s, children count: %s", postData.getText(), it.getChildren().size)
                }else {
                    holder.itemView.post_textview.text = String.format("postId: %s\npostDataType: %s", post.getPostId(), (postData as EkoPost.Data.CUSTOM).getDataType())
                    holder.itemView.data_textview.text = postData.getRawJson()?.toString()
                }

                holder.itemView.deleted_textview.text = String.format("deleted: %s", post.isDeleted())
                var childrenData = ""
                for (child in it.getChildren()) {
                    childrenData += "====================\n"
                    childrenData += String.format("postId: %s\npostDataType: %s\n", child.getPostId(), child.getDataType().apiKey)
                    childrenData += String.format("userId: %s\n", child.getPostedUserId())
                }
                childrenData += "====================\n"
                holder.itemView.data_children.text = childrenData
                holder.itemView.reaction_textview.text = String.format("reactionCount: %s\nmyReactions: %s", post.getReactionCount(), post.getMyReactions())
                holder.itemView.comment_count_textview.text = String.format("commentCount: %s", post.getCommentCount())
                holder.itemView.time_textview.text = String.format("created: %s", post.getCreatedAt())

                holder.itemView.setOnClickListener {
                    onClickSubject.onNext(post)
                }

                holder.itemView.setOnLongClickListener {
                    onLongClickSubject.onNext(post)
                    true
                }
            }
        }
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

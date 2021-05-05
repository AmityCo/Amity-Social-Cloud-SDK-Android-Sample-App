package com.amity.sample.ascsdk.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.social.comment.AmityComment
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_comment.view.*

class ReplyCommentListAdapter : RecyclerView.Adapter<ReplyCommentListAdapter.ReplyCommentViewHolder>() {
    private val onLongClickSubject = PublishSubject.create<AmityComment>()
    private val onClickSubject = PublishSubject.create<AmityComment>()
    private var commentList: List<AmityComment>? = null

    val onLongClickFlowable: Flowable<AmityComment>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<AmityComment>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyCommentViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ReplyCommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyCommentViewHolder, position: Int) {
        holder.bind(commentList?.get(position))
    }

    override fun getItemCount(): Int {
        return commentList?.size ?: 0
    }

    private fun getReferenceType(reference: AmityComment.Reference): String {
        return when (reference) {
            is AmityComment.Reference.CONTENT -> {
                "Content"
            }
            is AmityComment.Reference.POST -> {
                "Post"
            }
            else -> {
                "Unknown"
            }
        }
    }

    fun setComments(comment: List<AmityComment>?) {
        this.commentList = comment
        notifyDataSetChanged()
    }

    inner class ReplyCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: AmityComment?) {
            if (comment == null) {
                itemView.comment_id_textview.text = "loading..."
            } else {
                itemView.comment_id_textview.visibility = View.VISIBLE
                comment.let { commentNonNull ->
                    val data = commentNonNull.getData() as AmityComment.Data.TEXT
                    val text = StringBuilder()
                            .append("id: ")
                            .append(commentNonNull.getCommentId())
                            .append("\nreferenceType: ")
                            .append(getReferenceType(commentNonNull.getReference()))
                            .append("\ndata comment: ")
                            .append(data.getText())
                            .append("\nuser id: ")
                            .append(commentNonNull.getUser()?.getUserId() ?: "")
                            .append("\nparent id: ")
                            .append(commentNonNull.getParentId())
                            .append("\ndata type: ")
                            .append(commentNonNull.getDataType().apiKey)
                            .append("\nchildren number: ")
                            .append(commentNonNull.getChildrenNumber())
                            .append("\nreactions: ")
                            .append(commentNonNull.getReactionMap().toString())
                            .append("\nmy reactions: ")
                            .append(commentNonNull.getMyReactions().toString())
                            .append("\ndeleted: ")
                            .append(commentNonNull.isDeleted())
                            .append("\ncreated at: ")
                            .append(commentNonNull.getCreatedAt())
                            .toString()
                    itemView.comment_id_textview.text = text
                    itemView.setOnClickListener {
                        onClickSubject.onNext(commentNonNull)
                    }
                    itemView.setOnLongClickListener {
                        onLongClickSubject.onNext(commentNonNull)
                        true
                    }
                }
            }
        }
    }
}

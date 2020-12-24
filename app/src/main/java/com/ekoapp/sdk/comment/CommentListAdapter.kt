package com.ekoapp.sdk.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoCommentAdapter
import com.ekoapp.ekosdk.comment.EkoComment
import com.ekoapp.ekosdk.comment.EkoCommentReference
import com.ekoapp.sdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentListAdapter : EkoCommentAdapter<CommentListAdapter.CommentViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<EkoComment>()
    private val onClickSubject = PublishSubject.create<EkoComment>()

    val onLongClickFlowable: Flowable<EkoComment>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<EkoComment>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        if (comment == null) {
            holder.itemView.comment_id_textview.text = "loading..."
        } else {
            comment?.let { commentNonNull ->
                val data = commentNonNull.getData() as EkoComment.Data.TEXT
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
                        .toString()
                holder.itemView.comment_id_textview.text = text

                holder.itemView.setOnClickListener {
                    onClickSubject.onNext(commentNonNull)
                }

                holder.itemView.setOnLongClickListener {
                    onLongClickSubject.onNext(commentNonNull)
                    true
                }
            }
        }
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private fun getReferenceType(reference: EkoCommentReference): String {
        return when (reference) {
            is EkoCommentReference.Content -> {
                "Content"
            }
            is EkoCommentReference.Post -> {
                "Post"
            }
            else -> {
                "Unknown"
            }
        }
    }
}

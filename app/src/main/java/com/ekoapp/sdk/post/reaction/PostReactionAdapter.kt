package com.ekoapp.sdk.post.reaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.reaction.EkoReaction
import com.ekoapp.ekosdk.adapter.EkoReactionAdapter
import com.ekoapp.sdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_message_reaction.view.*

class PostReactionAdapter : EkoReactionAdapter<PostReactionAdapter.EkoPostReactionViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<EkoReaction>()

    internal val onLongClickFlowable: Flowable<EkoReaction>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EkoPostReactionViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_reaction, parent, false)
        return EkoPostReactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: EkoPostReactionViewHolder, position: Int) {
        getItem(position)?.let { messageReaction ->

            val userDisplayName = if (messageReaction.getUserDisplayName().isNullOrEmpty()) messageReaction.getUserId() else messageReaction.getUserDisplayName()
            val displayText = String.format("%s", userDisplayName)
            holder.userDisplayNameTextView.text = displayText
            holder.reactionNameTextView.text = messageReaction.getReactionName()
            holder.timestampTextView.text = String.format("%s", messageReaction.getCreatedAt())

            holder.itemView.setOnLongClickListener { v ->
                onLongClickSubject.onNext(messageReaction)
                true
            }
        }
    }

    class EkoPostReactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userDisplayNameTextView: TextView = itemView.user_display_name_textview

        var reactionNameTextView: TextView = itemView.reaction_name_textview

        var timestampTextView: TextView = itemView.timestamp_textview

    }
}


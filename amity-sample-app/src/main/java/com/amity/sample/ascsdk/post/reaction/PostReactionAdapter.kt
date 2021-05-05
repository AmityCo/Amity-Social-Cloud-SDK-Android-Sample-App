package com.amity.socialcloud.sdk.extension.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.reaction.AmityReaction
import com.amity.sample.ascsdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_message_reaction.view.*

class PostReactionAdapter : AmityReactionAdapter<PostReactionAdapter.AmityPostReactionViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<AmityReaction>()

    internal val onLongClickFlowable: Flowable<AmityReaction>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityPostReactionViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_reaction, parent, false)
        return AmityPostReactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmityPostReactionViewHolder, position: Int) {
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

    class AmityPostReactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userDisplayNameTextView: TextView = itemView.user_display_name_textview

        var reactionNameTextView: TextView = itemView.reaction_name_textview

        var timestampTextView: TextView = itemView.timestamp_textview

    }
}


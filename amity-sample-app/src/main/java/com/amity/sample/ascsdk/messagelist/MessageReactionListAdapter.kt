package com.amity.sample.ascsdk.messagelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.extension.adapter.AmityReactionAdapter
import com.amity.sample.ascsdk.R
import kotlinx.android.synthetic.main.item_message_reaction.view.*

class MessageReactionListAdapter : AmityReactionAdapter<MessageReactionListAdapter.AmityMessageReactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityMessageReactionViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_reaction, parent, false)
        return AmityMessageReactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmityMessageReactionViewHolder, position: Int) {
        getItem(position)?.let { messageReaction ->

            val userDisplayName = if(messageReaction.getUserDisplayName().isNullOrEmpty()) messageReaction.getUserId() else messageReaction.getUserDisplayName()
            val displayText = String.format("%s", userDisplayName)
            holder.userDisplayNameTextView.text = displayText
            holder.reactionNameTextView.text = messageReaction.getReactionName()
            holder.timestampTextView.text = String.format("%s", messageReaction.getCreatedAt())
        }
    }


    class AmityMessageReactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userDisplayNameTextView: TextView = itemView.user_display_name_textview

        var reactionNameTextView: TextView = itemView.reaction_name_textview

        var timestampTextView: TextView = itemView.timestamp_textview

    }

}
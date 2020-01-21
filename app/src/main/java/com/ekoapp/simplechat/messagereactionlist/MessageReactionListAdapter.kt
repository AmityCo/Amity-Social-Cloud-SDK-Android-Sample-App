package com.ekoapp.simplechat.messagereactionlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoMessageReactionAdapter
import com.ekoapp.simplechat.R
import kotlinx.android.synthetic.main.item_message_reaction.view.*

class MessageReactionListAdapter : EkoMessageReactionAdapter<MessageReactionListAdapter.EkoMessageReactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EkoMessageReactionViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_reaction, parent, false)
        return EkoMessageReactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: EkoMessageReactionViewHolder, position: Int) {
        getItem(position)?.let { messageReaction ->

            val userDisplayName = if(messageReaction.userDisplayName.isNullOrEmpty()) messageReaction.userId else messageReaction.userDisplayName
            val displayText = String.format("%s", userDisplayName)
            holder.userDisplayNameTextView.text = displayText
            holder.reactionNameTextView.text = messageReaction.reactionName
            holder.timestampTextView.text = String.format("%s", messageReaction.createdAt)
        }
    }


    class EkoMessageReactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userDisplayNameTextView: TextView = itemView.user_display_name_textview

        var reactionNameTextView: TextView = itemView.reaction_name_textview

        var timestampTextView: TextView = itemView.timestamp_textview

    }

}
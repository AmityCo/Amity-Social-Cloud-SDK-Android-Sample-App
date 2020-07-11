package com.ekoapp.sample.chatfeature.channels.list.viewholder

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.ekoapp.ekosdk.internal.data.model.EkoMessageReaction
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.ReactionData
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_user_reaction.view.*

class UserReactionViewHolder(itemView: View) : BaseViewHolder<EkoMessageReaction>(itemView) {

    override fun bind(item: EkoMessageReaction) {
        itemView.text_full_name.text = item.userId
    }

    fun renderIcon(context: Context, reaction: ReactionData?) {
        if (reaction != null) {
            itemView.image_reaction.visibility = View.VISIBLE
            Glide.with(context).load(reaction.icon)
                    .placeholder(R.drawable.ic_emoji_smile)
                    .into(itemView.image_reaction)
        } else {
            itemView.image_reaction.visibility = View.GONE
        }
    }
}
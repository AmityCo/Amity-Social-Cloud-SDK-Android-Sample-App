package com.ekoapp.sample.socialfeature.reactions.view.list.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.ekoapp.ekosdk.internal.data.model.EkoPostReaction
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import kotlinx.android.synthetic.main.component_avatar_reaction.view.*
import kotlinx.android.synthetic.main.item_user.view.text_full_name
import kotlinx.android.synthetic.main.item_user_reaction.view.*

class UserReactionFeedsViewHolder(itemView: View) : BaseViewHolder<EkoPostReaction>(itemView) {

    override fun bind(item: EkoPostReaction) {
        val context = itemView.context
        when (item.reactionName) {
            ReactionTypes.LIKE.text -> {
                itemView.avatar_reaction.image_reaction.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_see_like))
            }
            ReactionTypes.FAVORITE.text -> {
                itemView.avatar_reaction.image_reaction.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_see_favorite))
            }
        }

        itemView.text_full_name.text = item.userId
    }
}
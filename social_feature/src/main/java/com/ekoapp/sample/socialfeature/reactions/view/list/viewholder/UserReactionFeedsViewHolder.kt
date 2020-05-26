package com.ekoapp.sample.socialfeature.reactions.view.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.internal.data.model.EkoPostReaction
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_user.view.*

class UserReactionFeedsViewHolder(itemView: View) : BaseViewHolder<EkoPostReaction>(itemView) {

    override fun bind(item: EkoPostReaction) {
        itemView.text_full_name.text = item.userId
    }
}
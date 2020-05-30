package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_reaction.view.*

class ReactionsViewHolder(itemView: View) : BaseViewHolder<Int>(itemView) {

    override fun bind(item: Int) {
        val context = itemView.context
        itemView.image_reaction.setImageDrawable(ContextCompat.getDrawable(context, item))
    }

}
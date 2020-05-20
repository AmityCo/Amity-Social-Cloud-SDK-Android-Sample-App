package com.ekoapp.sample.socialfeature.userfeed.view.list.viewholder

import android.view.View
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_touchable_create_feeds.view.*

class CreateFeedsViewHolder(itemView: View) : BaseViewHolder<() -> Unit>(itemView) {
    override fun bind(item: () -> Unit) {
        itemView.button_touchable_target_post.setOnClickListener {
            item.invoke()
        }
    }
}
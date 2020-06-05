package com.ekoapp.sample.chatfeature.channels.list.viewholder

import android.view.View
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_about.view.*

class AboutChannelViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

    override fun bind(item: String) {
        itemView.text_about_content.text = item
    }
}
package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.view.View
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_checkbox_type.view.*

class CheckboxTypeViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

    override fun bind(item: String) {
        itemView.checkbox_type.text = item
    }
}
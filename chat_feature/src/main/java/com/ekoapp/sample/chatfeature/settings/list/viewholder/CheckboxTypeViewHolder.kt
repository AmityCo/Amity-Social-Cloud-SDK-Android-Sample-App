package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.view.View
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_checkbox_type.view.*

class CheckboxTypeViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

    companion object {
        private var selecteds = ArrayList<String>()
    }

    override fun bind(item: String) {
        itemView.checkbox_type.text = item
    }

    fun checked(action: (Set<String>) -> Unit) {
        val text = itemView.checkbox_type.text.toString()
        itemView.checkbox_type.setOnClickListener {
            if (itemView.checkbox_type.isChecked) {
                selecteds.add(text)
            } else {
                selecteds.remove(text)
            }
            action.invoke(selecteds.toSet())
        }
    }
}
package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.view.View
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.preferences.PreferenceHelper.channelTypes
import com.ekoapp.sample.core.preferences.PreferenceHelper.defaultPreference
import kotlinx.android.synthetic.main.item_checkbox_type.view.*

class CheckboxTypeViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

    companion object {
        private var selecteds = ArrayList<String>()
    }

    override fun bind(item: String) {
        val context = itemView.context
        val prefs = defaultPreference(context)
        itemView.checkbox_type.text = item
        prefs.channelTypes?.forEach {
            if (itemView.checkbox_type.text == it) itemView.checkbox_type.isChecked = true
        }
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
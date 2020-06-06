package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_checkbox_type.view.*

class CheckboxTypeViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

    companion object {
        private var selecteds = ArrayList<EkoChannel.Type>()
    }

    override fun bind(item: String) {
        itemView.checkbox_type.text = item
    }

    fun checked(action: (Set<EkoChannel.Type>) -> Unit, viewModel: ChannelSettingsViewModel) {
        val text = itemView.checkbox_type.text.toString()
        itemView.checkbox_type.setOnClickListener {
            if (itemView.checkbox_type.isChecked) {
                selecteds.add(viewModel.mapValue(text))
            } else {
                selecteds.remove(viewModel.mapValue(text))
            }
            action.invoke(selecteds.toSet())
        }
    }
}
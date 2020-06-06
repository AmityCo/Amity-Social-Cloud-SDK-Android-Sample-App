package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.view.View
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.activity_channel_query_filter.view.*

data class SaveSettingsViewData(val viewModel: ChannelSettingsViewModel)

class SaveSettingsViewHolder(itemView: View) : BaseViewHolder<() -> Unit>(itemView) {

    override fun bind(item: () -> Unit) {
        itemView.button_save.setOnClickListener {
            item.invoke()
        }
    }
}
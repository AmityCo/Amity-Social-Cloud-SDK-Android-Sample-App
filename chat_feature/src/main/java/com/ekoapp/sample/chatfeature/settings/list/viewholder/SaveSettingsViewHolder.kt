package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.view.View
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.core.base.list.BaseViewHolder

data class SaveSettingsViewData(val viewModel: ChannelSettingsViewModel)

class SaveSettingsViewHolder(itemView: View) : BaseViewHolder<SaveSettingsViewData>(itemView) {

    override fun bind(item: SaveSettingsViewData) {

    }
}
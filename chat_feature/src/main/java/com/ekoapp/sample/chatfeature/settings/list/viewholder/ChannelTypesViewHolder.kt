package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.content.Context
import android.view.View
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.chatfeature.settings.list.ChannelTypesAdapter
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import kotlinx.android.synthetic.main.item_settings_channel_types.view.*

data class ChannelTypesViewData(val viewModel: ChannelSettingsViewModel)

class ChannelTypesViewHolder(itemView: View) : BaseViewHolder<ChannelTypesViewData>(itemView) {

    private lateinit var adapter: ChannelTypesAdapter

    override fun bind(item: ChannelTypesViewData) {
        val context = itemView.context
        context.renderList(item.viewModel)
    }

    private fun Context.renderList(viewModel: ChannelSettingsViewModel) {
        adapter = ChannelTypesAdapter(this, viewModel.getChannelTypes(), viewModel)
        RecyclerBuilder(context = this, recyclerView = itemView.recycler_channel_types)
                .builder()
                .build(adapter)
    }
}
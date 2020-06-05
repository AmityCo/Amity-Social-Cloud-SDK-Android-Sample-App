package com.ekoapp.sample.chatfeature.settings.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.chatfeature.settings.list.viewholder.ChannelTypesData
import com.ekoapp.sample.chatfeature.settings.list.viewholder.ChannelTypesViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder


class MainChannelSettingsAdapter(private val context: Context,
                                 private val viewModel: ChannelSettingsViewModel) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private val sections = ArrayList<Any>()

    companion object {
        private const val TYPE_CHANNEL_TYPES = 0
    }

    init {
        sections.add(TYPE_CHANNEL_TYPES)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_CHANNEL_TYPES -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_settings_channel_types, parent, false)
                ChannelTypesViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ChannelTypesViewHolder -> {
                holder.bind(ChannelTypesData(viewModel))
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            sections[position] == TYPE_CHANNEL_TYPES -> {
                TYPE_CHANNEL_TYPES
            }
            else -> {
                TYPE_CHANNEL_TYPES
            }
        }
    }

    override fun getItemCount(): Int {
        return sections.size
    }
}

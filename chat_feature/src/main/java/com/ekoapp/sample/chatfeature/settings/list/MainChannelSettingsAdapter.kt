package com.ekoapp.sample.chatfeature.settings.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsViewModel
import com.ekoapp.sample.chatfeature.settings.list.viewholder.*
import com.ekoapp.sample.core.base.list.BaseViewHolder


class MainChannelSettingsAdapter(private val context: Context,
                                 private val viewModel: ChannelSettingsViewModel) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private val sections = ArrayList<Any>()

    companion object {
        private const val TYPE_CHANNEL_TYPES = 0
        private const val TYPE_MEMBERSHIP = 1
        private const val TYPE_INCLUDE_TAGS = 2
        private const val TYPE_EXCLUDE_TAGS = 3
    }

    init {
        sections.add(TYPE_CHANNEL_TYPES)
        sections.add(TYPE_MEMBERSHIP)
        sections.add(TYPE_INCLUDE_TAGS)
        sections.add(TYPE_EXCLUDE_TAGS)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_CHANNEL_TYPES -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_settings_channel_types, parent, false)
                ChannelTypesViewHolder(view)
            }
            TYPE_MEMBERSHIP -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_settings_membership, parent, false)
                MembershipViewHolder(view)
            }
            TYPE_INCLUDE_TAGS -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_settings_tags, parent, false)
                IncludeTagsViewHolder(view)
            }
            TYPE_EXCLUDE_TAGS -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_settings_tags, parent, false)
                ExcludeTagsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ChannelTypesViewHolder -> {
                holder.bind(ChannelTypesViewData(viewModel))
            }
            is MembershipViewHolder -> {
                holder.bind(viewModel)
            }
            is IncludeTagsViewHolder -> {
                holder.bind(TagsViewData(
                        title = R.string.temporarily_include_tags_display,
                        hint = R.string.temporarily_include_tags_hint))
            }
            is ExcludeTagsViewHolder -> {
                holder.bind(TagsViewData(
                        title = R.string.temporarily_exclude_tags_display,
                        hint = R.string.temporarily_exclude_tags_hint))
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            sections[position] == TYPE_CHANNEL_TYPES -> {
                TYPE_CHANNEL_TYPES
            }
            sections[position] == TYPE_MEMBERSHIP -> {
                TYPE_MEMBERSHIP
            }
            sections[position] == TYPE_INCLUDE_TAGS -> {
                TYPE_INCLUDE_TAGS
            }
            sections[position] == TYPE_EXCLUDE_TAGS -> {
                TYPE_EXCLUDE_TAGS
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

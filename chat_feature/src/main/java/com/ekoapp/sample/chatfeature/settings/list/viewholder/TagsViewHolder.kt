package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.view.View
import com.ekoapp.sample.chatfeature.settings.list.renders.renderEventTags
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.preferences.PreferenceHelper
import com.ekoapp.sample.core.preferences.PreferenceHelper.excludeTags
import com.ekoapp.sample.core.preferences.PreferenceHelper.includeTags
import kotlinx.android.synthetic.main.item_settings_membership.view.text_title
import kotlinx.android.synthetic.main.item_settings_tags.view.*


data class TagsViewData(val title: Int, val hint: Int)

class IncludeTagsViewHolder(itemView: View) : BaseViewHolder<TagsViewData>(itemView) {

    override fun bind(item: TagsViewData) {
        val context = itemView.context
        val prefs = PreferenceHelper.defaultPreference(context)
        prefs.includeTags?.forEach(itemView.edit_text_tags::setText)
        itemView.edit_text_tags.hint = context.getString(item.hint)
        itemView.text_title.text = context.getString(item.title)
    }

    fun includeTags(action: (Set<String>) -> Unit) = itemView.edit_text_tags.renderEventTags(action)
}

class ExcludeTagsViewHolder(itemView: View) : BaseViewHolder<TagsViewData>(itemView) {

    override fun bind(item: TagsViewData) {
        val context = itemView.context
        val prefs = PreferenceHelper.defaultPreference(context)
        prefs.excludeTags?.forEach(itemView.edit_text_tags::setText)
        itemView.text_title.text = context.getString(item.title)
        itemView.edit_text_tags.hint = context.getString(item.hint)
    }

    fun excludeTags(action: (Set<String>) -> Unit) = itemView.edit_text_tags.renderEventTags(action)
}
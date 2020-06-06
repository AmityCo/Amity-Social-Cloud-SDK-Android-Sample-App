package com.ekoapp.sample.chatfeature.settings.list.viewholder

import android.view.View
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_settings_membership.view.text_title
import kotlinx.android.synthetic.main.item_settings_tags.view.*


data class TagsViewData(val title: Int, val hint: Int)

class IncludeTagsViewHolder(itemView: View) : BaseViewHolder<TagsViewData>(itemView) {

    companion object {
        val tags = ArrayList<String>()
    }

    override fun bind(item: TagsViewData) {
        val context = itemView.context
        itemView.text_title.text = context.getString(item.title)
        itemView.edit_text_tags.hint = context.getString(item.hint)
    }

    fun includeTags(action: (Set<String>) -> Unit) {
        val text = itemView.edit_text_tags.text.toString()
        tags.add(text)
        action.invoke(tags.toSet())
    }
}

class ExcludeTagsViewHolder(itemView: View) : BaseViewHolder<TagsViewData>(itemView) {

    companion object {
        val tags = ArrayList<String>()
    }

    override fun bind(item: TagsViewData) {
        val context = itemView.context
        itemView.text_title.text = context.getString(item.title)
        itemView.edit_text_tags.hint = context.getString(item.hint)
    }

    fun excludeTags(action: (Set<String>) -> Unit) {
        val text = itemView.edit_text_tags.text.toString()
        IncludeTagsViewHolder.tags.add(text)
        action.invoke(IncludeTagsViewHolder.tags.toSet())
    }
}
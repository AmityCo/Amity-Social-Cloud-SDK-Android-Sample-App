package com.amity.sample.ascsdk.community.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.extension.adapter.AmityCommunityCategoryAdapter
import kotlinx.android.synthetic.main.item_categories.view.*

class CommunityCategoryAdapter : AmityCommunityCategoryAdapter<CommunityCategoryAdapter.AmityCommunityCategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityCommunityCategoriesViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_categories, parent, false)
        return AmityCommunityCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmityCommunityCategoriesViewHolder, position: Int) {
        val categories = getItem(position)
        if (categories == null) {
            holder.itemView.categories_textview.text = "loading..."
        } else {
            val text = StringBuilder()
                    .append("id: ")
                    .append(categories.getCategoryId())
                    .append("\nname: ")
                    .append(categories.getName())
                    .append("\navatarURL: ")
                    .append(categories.getAvatar()?.getUrl())
                    .append("\nMetadata: ")
                    .append(categories.getMetadata())
                    .append("\nisDeleted: ")
                    .append(categories.isDeleted())
                    .toString()
            holder.itemView.categories_textview.text = text
        }
    }

    class AmityCommunityCategoriesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}

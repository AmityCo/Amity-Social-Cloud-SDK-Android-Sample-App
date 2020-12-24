package com.ekoapp.sdk.community.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.adapter.EkoCommunityCategoryAdapter
import com.ekoapp.sdk.R
import kotlinx.android.synthetic.main.item_categories.view.*

class CommunityCategoryAdapter : EkoCommunityCategoryAdapter<CommunityCategoryAdapter.EkoCommunityCategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EkoCommunityCategoriesViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_categories, parent, false)
        return EkoCommunityCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EkoCommunityCategoriesViewHolder, position: Int) {
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
                    .append(categories.getDeleted())
                    .toString()
            holder.itemView.categories_textview.text = text
        }
    }

    class EkoCommunityCategoriesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}

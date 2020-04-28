package com.ekoapp.sample.socialfeature.userfeed.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.core.base.list.ViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import kotlinx.android.synthetic.main.component_body_feeds.view.*
import kotlinx.android.synthetic.main.component_header_feeds.view.*

class UserFeedsAdapter(private val context: Context,
                       private val items: List<SampleFeedsResponse>) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_feeds, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val itemView = holder.itemView
        val context = itemView.context

        itemView.text_full_name.text = item.creator
        itemView.text_description.text = item.description
    }
}


package com.ekoapp.sample.socialfeature.userfeed.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.core.base.list.ViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.UPPERMOST
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeed.view.renders.UserFeedsRenderData
import com.ekoapp.sample.socialfeature.userfeed.view.renders.userFeedRender
import kotlinx.android.synthetic.main.item_user_feeds.view.*


class UserFeedsAdapter(private val context: Context,
                       private val items: MutableList<SampleFeedsResponse>,
                       private val userFeedsViewModel: UserFeedsViewModel) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_feeds, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedsResponse = items[position]
        val itemView = holder.itemView
        val context = itemView.context

        UserFeedsRenderData(context, feedsResponse).userFeedRender(
                header = itemView.header_feeds,
                body = itemView.body_feeds,
                eventDelete = {
                    userFeedsViewModel.submitDelete(feedsResponse.id)
                })
    }

    fun addItem(position: Int = UPPERMOST, data: SampleFeedsResponse) {
        items.add(position, data)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, items.size)
    }
}


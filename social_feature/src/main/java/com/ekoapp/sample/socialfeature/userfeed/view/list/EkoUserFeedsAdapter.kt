package com.ekoapp.sample.socialfeature.userfeed.view.list


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.base.list.ViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeed.view.renders.EkoUserFeedsRenderData
import com.ekoapp.sample.socialfeature.userfeed.view.renders.userFeedRender
import kotlinx.android.synthetic.main.item_user_feeds.view.*

class EkoUserFeedsAdapter(private val items: PagedList<EkoPost>,
                          private val userFeedsViewModel: UserFeedsViewModel) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_feeds, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val itemView = holder.itemView
        val context = itemView.context

        EkoUserFeedsRenderData(context, item).userFeedRender(
                header = itemView.header_feeds,
                body = itemView.body_feeds,
                eventEdit = {

                },
                eventDelete = {

                })
    }
}


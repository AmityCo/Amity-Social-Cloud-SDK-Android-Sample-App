package com.ekoapp.sample.socialfeature.userfeed.view.list


import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoUserAdapter
import com.ekoapp.sample.core.base.list.ViewHolder
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.item_friend_feeds.view.*

class EkoFriendsFeedsAdapter : EkoUserAdapter<ViewHolder>() {
    private val displayItem = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_friend_feeds, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val itemView = holder.itemView
        itemView.text_full_name.text = item?.userId
    }

    override fun getItemCount(): Int {
        return displayItem
    }
}


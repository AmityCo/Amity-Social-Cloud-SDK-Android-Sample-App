package com.ekoapp.sample.socialfeature.userfeeds.view.list


import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoUserAdapter
import com.ekoapp.sample.core.base.list.ViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.item_friend_feeds.view.*

class EkoFriendsFeedsAdapter(val viewModel: UserFeedsViewModel) : EkoUserAdapter<ViewHolder>() {
    private val displayItem = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_friend_feeds, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val itemView = holder.itemView
        item?.userId?.apply {
            itemView.text_full_name.text = this
            itemView.setOnClickListener {
                viewModel.usersActionRelay.postValue(UserData(userId = this))
            }
        }
    }

    override fun getItemCount(): Int {
        return displayItem
    }
}


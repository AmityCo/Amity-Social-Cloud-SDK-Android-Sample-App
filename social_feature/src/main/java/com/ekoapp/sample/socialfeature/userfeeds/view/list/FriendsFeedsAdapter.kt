package com.ekoapp.sample.socialfeature.userfeeds.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoUserAdapter
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder.FriendsFeedsData
import com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder.FriendsFeedsViewHolder

class FriendsFeedsAdapter(private val context: Context, val viewModel: UserFeedsViewModel) : EkoUserAdapter<BaseViewHolder<*>>() {
    private val displayItem = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_friend_feeds, parent, false)
        return FriendsFeedsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is FriendsFeedsViewHolder -> {
                getItem(position)?.apply {
                    holder.bind(FriendsFeedsData(this, viewModel))
                }
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return displayItem
    }
}

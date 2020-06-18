package com.ekoapp.sample.socialfeature.userfeeds.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoPostAdapter
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeeds.data.FeedsData
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder.FeedsViewData
import com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder.FeedsViewHolder
import com.ekoapp.sample.socialfeature.users.data.UserData

class FeedsAdapter(
        private val context: Context,
        private val userData: UserData,
        private val viewModel: UserFeedsViewModel) : EkoPostAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_feeds, parent, false)
        return FeedsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is FeedsViewHolder -> {
                getItem(position)?.apply {
                    holder.bind(FeedsViewData(userData, this, viewModel))
                    holder.itemView.setOnClickListener {
                        viewModel.renderFeedsById(FeedsData(postId = postId))
                    }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}


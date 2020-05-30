package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.view.View
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.item_touchable_create_feeds.view.*

class CreateFeedsData(val userData: UserData, val viewModel: UserFeedsViewModel)

class CreateFeedsViewHolder(itemView: View) : BaseViewHolder<CreateFeedsData>(itemView) {
    override fun bind(item: CreateFeedsData) {
        itemView.button_touchable_target_post.setOnClickListener {
            item.viewModel.createFeedsActionRelay.postValue(item.userData)
        }
    }
}
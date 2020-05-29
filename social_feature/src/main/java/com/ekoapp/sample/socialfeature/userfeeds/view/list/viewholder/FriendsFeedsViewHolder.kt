package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.item_user.view.*

data class FriendsFeedsData(val item: EkoUser, val viewModel: UserFeedsViewModel)

class FriendsFeedsViewHolder(itemView: View) : BaseViewHolder<FriendsFeedsData>(itemView) {

    override fun bind(item: FriendsFeedsData) {
        item.apply {
            val userId = this.item.userId
            itemView.text_full_name.text = this.item.userId
            itemView.setOnClickListener {
                viewModel.usersActionRelay.postValue(UserData(userId = userId))
            }
        }
    }

}
package com.ekoapp.sample.socialfeature.users.view.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.users.data.UserData
import com.ekoapp.sample.socialfeature.users.view.UsersViewModel
import com.ekoapp.sample.socialfeature.users.view.renders.EkoUsersRenderData
import com.ekoapp.sample.socialfeature.users.view.renders.usersRender
import kotlinx.android.synthetic.main.item_user.view.*

data class UsersData(val item: EkoUser, val viewModel: UsersViewModel)

class UsersViewHolder(itemView: View) : BaseViewHolder<UsersData>(itemView) {

    override fun bind(item: UsersData) {
        item.apply {
            EkoUsersRenderData(item = this.item).usersRender(
                    itemView,
                    body = itemView.text_full_name,
                    eventClick = {
                        this.viewModel.usersActionRelay.postValue(UserData(userId = it.item.userId))
                    })
        }
    }

}
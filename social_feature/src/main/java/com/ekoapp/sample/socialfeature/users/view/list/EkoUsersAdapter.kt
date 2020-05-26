package com.ekoapp.sample.socialfeature.users.view.list


import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoUserAdapter
import com.ekoapp.sample.core.base.list.ViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.users.data.UserData
import com.ekoapp.sample.socialfeature.users.view.UsersViewModel
import com.ekoapp.sample.socialfeature.users.view.renders.EkoUsersRenderData
import com.ekoapp.sample.socialfeature.users.view.renders.usersRender
import kotlinx.android.synthetic.main.item_friend_feeds.view.*

class EkoUsersAdapter(private val viewModel: UsersViewModel) : EkoUserAdapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val itemView = holder.itemView
        val context = itemView.context
        item?.apply {
            EkoUsersRenderData(context, this).usersRender(
                    itemView,
                    body = itemView.text_full_name,
                    eventClick = {
                        viewModel.usersActionRelay.postValue(UserData(userId = it.item.userId))
                    })
        }
    }
}


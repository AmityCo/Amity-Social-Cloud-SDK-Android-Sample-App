package com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder

import android.view.View
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.item_profile_feeds.view.*

class ProfileViewHolder(itemView: View) : BaseViewHolder<UserData>(itemView) {

    override fun bind(item: UserData) {
        itemView.text_full_name.text = item.userId
    }

}
package com.ekoapp.sample.chatfeature.channels.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.chatfeature.constants.ZERO_COUNT
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_user_conversation.view.*

class UserChannelsViewHolder(itemView: View) : BaseViewHolder<EkoUser>(itemView) {

    override fun bind(item: EkoUser) {
        //TODO Check and get total unread each user
        itemView.avatar_with_total.setupView(ZERO_COUNT)
        itemView.text_full_name.text = item.userId
    }

    fun onClick(action: () -> Unit) {
        itemView.setOnClickListener { action.invoke() }
    }
}
package com.ekoapp.sample.chatfeature.channels.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_suggestion_user.view.*

class UserChannelsViewHolder(itemView: View) : BaseViewHolder<EkoUser>(itemView) {

    override fun bind(item: EkoUser) {
        itemView.text_full_name.text = item.userId
    }

    fun onClick(action: () -> Unit) {
        itemView.setOnClickListener { action.invoke() }
    }
}
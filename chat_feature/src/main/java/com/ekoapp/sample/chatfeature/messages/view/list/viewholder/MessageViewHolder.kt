package com.ekoapp.sample.chatfeature.messages.view.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.messages.view.list.renders.MessageRenderData
import com.ekoapp.sample.chatfeature.messages.view.list.renders.renderMessage
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_main_message.view.*

class MessageViewHolder(itemView: View) : BaseViewHolder<EkoMessage>(itemView) {

    override fun bind(item: EkoMessage) {
        val context = itemView.context
        val iAMSender = item.userId == EkoClient.getUserId()
        MessageRenderData(context = context, item = item, iAMSender = iAMSender)
                .renderMessage(
                        itemView.text_time,
                        itemView.text_message,
                        itemView.image_message,
                        itemView.file_message)
    }
}
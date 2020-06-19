package com.ekoapp.sample.chatfeature.messages.view.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.messages.view.MessagesViewModel
import com.ekoapp.sample.chatfeature.messages.view.list.renders.MessageRenderData
import com.ekoapp.sample.chatfeature.messages.view.list.renders.renderMessage
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_main_message.view.*

data class MessageViewData(val item: EkoMessage, val viewModel: MessagesViewModel)

class MessageViewHolder(itemView: View) : BaseViewHolder<MessageViewData>(itemView) {
    private val context = itemView.context

    override fun bind(item: MessageViewData) {
        val iAMSender = item.item.userId == EkoClient.getUserId()
        MessageRenderData(
                context = context,
                item = item.item,
                iAMSender = iAMSender,
                reactions = item.viewModel.getReactions())
                .renderMessage(
                        itemView.text_time,
                        itemView.text_message,
                        itemView.image_message,
                        itemView.file_message,
                        item.viewModel::renderReplying)
    }
}
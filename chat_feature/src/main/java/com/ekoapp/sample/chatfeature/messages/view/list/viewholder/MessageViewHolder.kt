package com.ekoapp.sample.chatfeature.messages.view.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.messages.view.MessagesViewModel
import com.ekoapp.sample.chatfeature.messages.view.list.ReactionsAdapter
import com.ekoapp.sample.chatfeature.messages.view.list.renders.MessageRenderData
import com.ekoapp.sample.chatfeature.messages.view.list.renders.renderMessage
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import kotlinx.android.synthetic.main.component_text_message.view.*
import kotlinx.android.synthetic.main.item_main_message.view.*

data class MessageViewData(val item: EkoMessage, val viewModel: MessagesViewModel)

class MessageViewHolder(itemView: View) : BaseViewHolder<MessageViewData>(itemView) {
    private val context = itemView.context
    private lateinit var adapter: ReactionsAdapter

    override fun bind(item: MessageViewData) {
        val iAMSender = item.item.userId == EkoClient.getUserId()
        MessageRenderData(context = context, item = item.item, iAMSender = iAMSender)
                .renderMessage(
                        itemView.text_time,
                        itemView.text_message,
                        itemView.image_message,
                        itemView.file_message,
                        item.viewModel::renderReplying)
        renderSelectReactions(item)
    }

    private fun renderSelectReactions(item: MessageViewData) {
        adapter = ReactionsAdapter(context, item.viewModel.getReactions(), item.viewModel)
        RecyclerBuilder(context, itemView.recycler_reactions, item.viewModel.getReactions().size)
                .builder()
                .build(adapter)
    }
}
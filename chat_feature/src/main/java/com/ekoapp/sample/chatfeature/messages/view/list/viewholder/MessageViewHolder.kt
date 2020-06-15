package com.ekoapp.sample.chatfeature.messages.view.list.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.messages.view.MessagesViewModel
import com.ekoapp.sample.chatfeature.messages.view.list.ReactionsAdapter
import com.ekoapp.sample.chatfeature.messages.view.list.renders.MessageRenderData
import com.ekoapp.sample.chatfeature.messages.view.list.renders.renderMessage
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import kotlinx.android.synthetic.main.component_image_message.view.*
import kotlinx.android.synthetic.main.component_text_message.view.*
import kotlinx.android.synthetic.main.item_main_message.view.*

data class MessageViewData(val item: EkoMessage, val viewModel: MessagesViewModel)

class MessageViewHolder(itemView: View) : BaseViewHolder<MessageViewData>(itemView) {
    private val context = itemView.context

    override fun bind(item: MessageViewData) {
        val iAMSender = item.item.userId == EkoClient.getUserId()
        MessageRenderData(context = context, item = item.item, iAMSender = iAMSender)
                .renderMessage(
                        itemView.text_time,
                        itemView.text_message,
                        itemView.image_message,
                        itemView.file_message,
                        item.viewModel::renderReplying)
        itemView.recycler_reactions_text.renderSelectReactions(item.viewModel)
        itemView.recycler_reactions_image.renderSelectReactions(item.viewModel)
    }

    private fun RecyclerView.renderSelectReactions(viewModel: MessagesViewModel) {
        val adapter: ReactionsAdapter
        val items = viewModel.getReactions()
        adapter = ReactionsAdapter(context, items, viewModel)
        RecyclerBuilder(context, this, items.size)
                .builder()
                .build(adapter)
    }
}
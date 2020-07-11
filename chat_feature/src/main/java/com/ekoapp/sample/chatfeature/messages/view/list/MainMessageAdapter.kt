package com.ekoapp.sample.chatfeature.messages.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.ekoapp.ekosdk.adapter.EkoMessageAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.MessageData
import com.ekoapp.sample.chatfeature.messages.view.MessagesViewModel
import com.ekoapp.sample.chatfeature.messages.view.list.viewholder.MessageViewData
import com.ekoapp.sample.chatfeature.messages.view.list.viewholder.MessageViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder

class MainMessageAdapter(private val context: Context,
                         private val lifecycleOwner: LifecycleOwner,
                         private val viewModel: MessagesViewModel) : EkoMessageAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_main_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                getItem(position)?.let {
                    holder.bind(MessageViewData(it, lifecycleOwner, viewModel))
                    holder.clickViewReply {
                        viewModel.renderViewReply(MessageData(channelId = it.channelId, parentId = it.messageId))
                    }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

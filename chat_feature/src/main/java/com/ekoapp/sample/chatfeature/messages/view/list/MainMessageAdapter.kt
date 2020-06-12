package com.ekoapp.sample.chatfeature.messages.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoMessageAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.messages.view.MessagesViewModel
import com.ekoapp.sample.chatfeature.messages.view.list.viewholder.MessageViewData
import com.ekoapp.sample.chatfeature.messages.view.list.viewholder.MessageViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder

class MainMessageAdapter(private val context: Context, val viewModel: MessagesViewModel) : EkoMessageAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_main_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                getItem(position)?.let {
                    holder.bind(MessageViewData(it, viewModel))
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}

package com.ekoapp.sample.chatfeature.channels.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoUserAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channels.ChannelsViewModel
import com.ekoapp.sample.chatfeature.channels.list.viewholder.UserChannelsViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder


class ConversationWithUserAdapter(private val context: Context,
                                  private val viewModel: ChannelsViewModel,
                                  private val isClicked: (Boolean) -> Unit) : EkoUserAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_conversation, parent, false)
        return UserChannelsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is UserChannelsViewHolder -> {
                getItem(position)?.apply {
                    holder.bind(this)
                    holder.onClick {
                        isClicked.invoke(true)
                        viewModel.bindCreateConversation(this.userId)
                    }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}

package com.ekoapp.sample.chatfeature.channels.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoMessageReactionAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channels.list.viewholder.UserReactionViewHolder
import com.ekoapp.sample.chatfeature.data.ReactionData
import com.ekoapp.sample.core.base.list.BaseViewHolder


class UsersWithReactionsAdapter(private val context: Context, val items: ArrayList<ReactionData>) : EkoMessageReactionAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_reaction, parent, false)
        return UserReactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is UserReactionViewHolder -> {
                getItem(position)?.apply {
                    val reactionData = items.reversed().lastOrNull { it.name == this.reactionName }
                    holder.bind(this)
                    holder.renderIcon(context, reactionData)
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}

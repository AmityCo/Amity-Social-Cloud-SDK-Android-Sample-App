package com.ekoapp.sample.socialfeature.reactions.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoPostReactionAdapter
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.reactions.view.list.viewholder.UserReactionFeedsViewHolder

class EkoUserReactionFeedsAdapter(private val context: Context) : EkoPostReactionAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_reaction, parent, false)
        return UserReactionFeedsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        getItem(position)?.apply {
            when (holder) {
                is UserReactionFeedsViewHolder -> {
                    holder.bind(item = this)
                }
            }
        }
    }
}


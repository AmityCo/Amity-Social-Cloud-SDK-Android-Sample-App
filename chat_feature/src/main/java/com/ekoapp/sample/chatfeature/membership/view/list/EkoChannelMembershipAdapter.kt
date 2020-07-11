package com.ekoapp.sample.chatfeature.membership.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoChannelMembershipAdapter
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.membership.view.MembershipViewModel
import com.ekoapp.sample.chatfeature.membership.view.list.viewholder.ChannelMembershipViewData
import com.ekoapp.sample.chatfeature.membership.view.list.viewholder.ChannelMembershipViewHolder
import com.ekoapp.sample.core.base.list.BaseViewHolder

class EkoChannelMembershipAdapter(private val context: Context,
                                  private val viewModel: MembershipViewModel) : EkoChannelMembershipAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_channel_member, parent, false)
        return ChannelMembershipViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ChannelMembershipViewHolder -> {
                getItem(position)?.apply {
                    holder.bind(ChannelMembershipViewData(this, viewModel))
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}


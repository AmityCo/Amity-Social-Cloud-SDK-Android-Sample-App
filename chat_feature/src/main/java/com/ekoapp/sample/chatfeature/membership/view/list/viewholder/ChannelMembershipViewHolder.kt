package com.ekoapp.sample.chatfeature.membership.view.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoChannelMembership
import com.ekoapp.sample.chatfeature.membership.view.MembershipViewModel
import com.ekoapp.sample.chatfeature.membership.view.renders.EkoChannelMembershipRenderData
import com.ekoapp.sample.chatfeature.membership.view.renders.channelMembershipRender
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_channel_member.view.*

data class ChannelMembershipViewData(val item: EkoChannelMembership, val viewModel: MembershipViewModel)

class ChannelMembershipViewHolder(itemView: View) : BaseViewHolder<ChannelMembershipViewData>(itemView) {
    override fun bind(item: ChannelMembershipViewData) {
        item.apply {
            EkoChannelMembershipRenderData(item = this.item).channelMembershipRender(body = itemView.text_full_name)
        }
    }

}
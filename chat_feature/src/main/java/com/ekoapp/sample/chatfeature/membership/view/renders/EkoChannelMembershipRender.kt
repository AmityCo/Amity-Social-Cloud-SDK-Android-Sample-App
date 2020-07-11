package com.ekoapp.sample.chatfeature.membership.view.renders

import android.widget.TextView
import com.ekoapp.ekosdk.EkoChannelMembership

data class EkoChannelMembershipRenderData(val item: EkoChannelMembership)

fun EkoChannelMembershipRenderData.channelMembershipRender(body: TextView) {
    body.text = item.userId
}
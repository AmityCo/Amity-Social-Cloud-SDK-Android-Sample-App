package com.ekoapp.sample.chatfeature.channels.list.viewholder

import android.view.View
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.sample.chatfeature.channels.list.renders.EkoChannelsRenderData
import com.ekoapp.sample.chatfeature.channels.list.renders.channelRender
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_channel.view.*

class ChannelsViewHolder(itemView: View) : BaseViewHolder<EkoChannel>(itemView) {

    override fun bind(item: EkoChannel) {
        val context = itemView.context
        EkoChannelsRenderData(context, item).channelRender(
                itemView.avatar_chat_room_with_total,
                itemView.image_chat_room_type,
                itemView.text_chat_room_name,
                itemView.text_chat_room_member,
                itemView.image_more_horiz
        )
    }

}
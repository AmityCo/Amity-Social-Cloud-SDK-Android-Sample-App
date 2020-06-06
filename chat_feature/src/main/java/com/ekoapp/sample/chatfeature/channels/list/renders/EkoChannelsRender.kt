package com.ekoapp.sample.chatfeature.channels.list.renders

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.components.AvatarChatRoomWithTotalComponent
import com.ekoapp.sample.chatfeature.enums.ChannelType


data class EkoChannelsRenderData(val context: Context, val item: EkoChannel)

fun EkoChannelsRenderData.channelRender(avatar: AvatarChatRoomWithTotalComponent,
                                        chatRoomType: ImageView,
                                        name: TextView,
                                        member: TextView) {

    avatar.setupView(item.unreadCount)

    //TODO Refactor
    when (item.channelType) {
        ChannelType.STANDARD.text -> {
            chatRoomType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_channel_public))
        }
        ChannelType.PRIVATE.text -> {
            chatRoomType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_channel_private))
        }
        ChannelType.BROADCAST.text -> {
            chatRoomType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_channel_broadcast))
        }
        ChannelType.CONVERSATION.text -> {
            chatRoomType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_channel_chat))
        }
    }

    if (item.id.isNullOrBlank()) {
        name.text = context.getString(R.string.temporarily_anonymous)
    } else {
        name.text = item.id
    }

    member.text = String.format(context.getString(R.string.temporarily_members), item.memberCount)

}
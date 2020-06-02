package com.ekoapp.sample.chatfeature.channels.list.renders

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.components.AvatarChatRoomWithTotalComponent
import com.ekoapp.sample.chatfeature.enums.ChannelTypes


data class EkoChannelsRenderData(val context: Context, val item: EkoChannel)

fun EkoChannelsRenderData.channelRender(avatar: AvatarChatRoomWithTotalComponent,
                                        chatRoomType: ImageView,
                                        name: TextView,
                                        member: TextView) {

    avatar.setupView(item.unreadCount)

    //TODO Refactor
    when (item.channelType) {
        ChannelTypes.STANDARD.text -> {
            chatRoomType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_channel_public))
        }
        ChannelTypes.PRIVATE.text -> {
            chatRoomType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_channel_private))
        }
        ChannelTypes.BROADCAST.text -> {
            chatRoomType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_channel_broadcast))
        }
        ChannelTypes.CONVERSATION.text -> {
            chatRoomType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_channel_chat))
        }
    }

    if (item.displayName.isNullOrBlank()) {
        name.text = context.getString(R.string.temporarily_anonymous)
    } else {
        name.text = item.displayName
    }

    member.text = String.format(context.getString(R.string.temporarily_members), item.memberCount)

}
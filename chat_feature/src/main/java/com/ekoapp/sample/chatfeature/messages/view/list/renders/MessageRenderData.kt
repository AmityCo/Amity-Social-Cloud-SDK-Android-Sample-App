package com.ekoapp.sample.chatfeature.messages.view.list.renders

import android.content.Context
import android.view.View
import android.widget.TextView
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.messaging.data.DataType
import com.ekoapp.sample.chatfeature.components.ImageMessageComponent
import com.ekoapp.sample.chatfeature.components.TextMessageComponent
import com.ekoapp.sample.chatfeature.messages.sealed.MessageSealed
import com.ekoapp.sample.core.utils.getTimeAgo

data class MessageRenderData(val context: Context, val item: EkoMessage)

fun EkoMessage.getMessageSealed(): MessageSealed {
    return when {
        DataType.TEXT == DataType.from(type) -> MessageSealed.Text(this)
        DataType.IMAGE == DataType.from(type) -> MessageSealed.Image(this)
        DataType.FILE == DataType.from(type) -> MessageSealed.File(this)
        DataType.CUSTOM == DataType.from(type) -> MessageSealed.Custom(this)
        else -> MessageSealed.Text(this)
    }
}

fun MessageRenderData.messageRender(textTime: TextView,
                                    textMessage: TextMessageComponent,
                                    imageMessage: ImageMessageComponent) {

    textTime.text = item.createdAt.toDate().getTimeAgo()
    when (item.getMessageSealed()) {
        is MessageSealed.Text -> {
            textMessage.visibility = View.VISIBLE
            imageMessage.visibility = View.GONE
            textMessage.setupView(item)
        }
        is MessageSealed.Image -> {
            imageMessage.visibility = View.VISIBLE
            textMessage.visibility = View.GONE
            imageMessage.setupView(item)
        }
        is MessageSealed.File -> {

        }
        is MessageSealed.Custom -> {

        }
    }
}
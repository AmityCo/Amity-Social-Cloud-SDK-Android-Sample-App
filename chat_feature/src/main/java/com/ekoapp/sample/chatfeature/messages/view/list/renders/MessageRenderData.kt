package com.ekoapp.sample.chatfeature.messages.view.list.renders

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.messaging.data.DataType
import com.ekoapp.sample.chatfeature.components.FileMessageComponent
import com.ekoapp.sample.chatfeature.components.ImageMessageComponent
import com.ekoapp.sample.chatfeature.components.TextMessageComponent
import com.ekoapp.sample.chatfeature.messages.seals.MessageSealed
import com.ekoapp.sample.core.utils.getTimeAgo


data class MessageRenderData(val context: Context, val item: EkoMessage, val iAMSender: Boolean)

fun EkoMessage.getMessageSealed(): MessageSealed {
    return when {
        DataType.TEXT == DataType.from(type) -> MessageSealed.Text(this)
        DataType.IMAGE == DataType.from(type) -> MessageSealed.Image(this)
        DataType.FILE == DataType.from(type) -> MessageSealed.File(this)
        DataType.CUSTOM == DataType.from(type) -> MessageSealed.Custom(this)
        else -> MessageSealed.Text(this)
    }
}

fun MessageRenderData.renderMessage(textTime: TextView,
                                    textMessage: TextMessageComponent,
                                    imageMessage: ImageMessageComponent,
                                    fileMessage: FileMessageComponent,
                                    eventReply: (EkoMessage) -> Unit) {

    textTime.text = item.createdAt.toDate().getTimeAgo()

    when (item.getMessageSealed()) {
        is MessageSealed.Text -> {
            textMessage.visibility = View.VISIBLE
            imageMessage.visibility = View.GONE
            fileMessage.visibility = View.GONE
            textMessage.apply {
                setMessage(item, eventReply::invoke)
                renderLayoutSenderAndReceiver(iAMSender)
                iAMSender.showOrHideAvatar()
            }
        }
        is MessageSealed.Image -> {
            imageMessage.visibility = View.VISIBLE
            textMessage.visibility = View.GONE
            fileMessage.visibility = View.GONE
            imageMessage.apply {
                setMessage(item, eventReply::invoke)
                renderLayoutSenderAndReceiver(iAMSender)
                iAMSender.showOrHideAvatar()
            }
        }
        is MessageSealed.File -> {
            fileMessage.visibility = View.VISIBLE
            textMessage.visibility = View.GONE
            imageMessage.visibility = View.GONE
            fileMessage.apply {
                setMessage(item, eventReply::invoke)
                renderLayoutSenderAndReceiver(iAMSender)
                iAMSender.showOrHideAvatar()
            }
        }
        is MessageSealed.Custom -> {
            textMessage.visibility = View.VISIBLE
            imageMessage.visibility = View.GONE
            fileMessage.visibility = View.GONE
            textMessage.apply {
                setMessage(item, eventReply::invoke)
                renderLayoutSenderAndReceiver(iAMSender)
                iAMSender.showOrHideAvatar()
            }
        }
    }
}

private fun ConstraintLayout.renderLayoutSenderAndReceiver(iAMSender: Boolean) {
    val params = layoutParams as ConstraintLayout.LayoutParams
    if (iAMSender) {
        params.startToStart = -1
        params.endToEnd = 0
    } else {
        params.startToStart = 0
        params.endToEnd = -1
    }

    requestLayout()
}
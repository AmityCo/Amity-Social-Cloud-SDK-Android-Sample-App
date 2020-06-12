package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.messaging.data.FileData
import com.ekoapp.ekosdk.messaging.data.ImageData
import com.ekoapp.ekosdk.messaging.data.TextData
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.SendMessageData
import com.ekoapp.sample.chatfeature.messages.seals.MessageSealed
import com.ekoapp.sample.chatfeature.messages.view.list.renders.getMessageSealed
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.component_main_send_message.view.*

class MainSendMessageComponent : ConstraintLayout {

    private val textRelay = PublishProcessor.create<SendMessageData>()

    fun text() = textRelay

    init {
        LayoutInflater.from(context).inflate(R.layout.component_main_send_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun textRender(channelId: String) {
        send_message.sendMessage {
            textRelay.onNext(SendMessageData(
                    channelId = channelId,
                    text = it))
        }
    }

    fun replyingRender(item: EkoMessage) {
        when (item.getMessageSealed()) {
            is MessageSealed.Text -> {
                textRelay.onNext(
                        SendMessageData(
                                channelId = item.channelId,
                                parentId = item.messageId,
                                text = item.getData(TextData::class.java).text))
            }
            is MessageSealed.Image -> {
                textRelay.onNext(
                        SendMessageData(
                                channelId = item.channelId,
                                parentId = item.messageId,
                                text = item.getData(ImageData::class.java).url))
            }
            is MessageSealed.File -> {
                textRelay.onNext(
                        SendMessageData(
                                channelId = item.channelId,
                                parentId = item.messageId,
                                text = item.getData(FileData::class.java).url))
            }
            is MessageSealed.Custom -> {
                textRelay.onNext(
                        SendMessageData(
                                channelId = item.channelId,
                                parentId = item.messageId,
                                text = item.getData(TextData::class.java).text))
            }
        }

        replying_to.visibility = View.VISIBLE
        replying_to.setView(item)
        replying_to.cancelReplying {
            replying_to.visibility = View.GONE
        }
    }
}
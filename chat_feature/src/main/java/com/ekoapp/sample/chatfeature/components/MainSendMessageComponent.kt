package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.internal.util.RealPathUtil
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.ReplyingStateData
import com.ekoapp.sample.chatfeature.data.SendMessageData
import com.ekoapp.sample.core.utils.getRealUri
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.component_main_send_message.view.*


class MainSendMessageComponent : ConstraintLayout {
    private val textRelay = PublishProcessor.create<SendMessageData>()
    private val replyingStateRelay = PublishProcessor.create<ReplyingStateData>()
    private var currentPhotoPath: String = ""

    fun message() = textRelay
    fun replyingState() = replyingStateRelay

    init {
        LayoutInflater.from(context).inflate(R.layout.component_main_send_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun renderTextSending(item: ReplyingStateData) {
        send_message.textMessage(sent = {
            hideReplying()
            if (item.isNotCancel) {
                textRelay.onNext(SendMessageData(channelId = item.channelId, parentId = item.parentId, text = it))
            } else {
                if (item.isReplyPage) {
                    textRelay.onNext(SendMessageData(channelId = item.channelId, parentId = item.parentId, text = it))
                } else {
                    textRelay.onNext(SendMessageData(channelId = item.channelId, parentId = null, text = it))
                }
            }
        })
    }

    fun renderSelectPhoto(fm: FragmentManager) = send_message.imageMessage(fm) { currentPhotoPath = it }

    fun renderImageSending(item: ReplyingStateData, uri: Uri? = null) {
        if (uri != null) currentPhotoPath = RealPathUtil.getRealPath(context, uri)
        send_image.visibility = View.VISIBLE
        send_image.setupView(currentPhotoPath, sent = {
            send_image.visibility = View.GONE
            if (item.isNotCancel) {
                textRelay.onNext(
                        SendMessageData(channelId = item.channelId, parentId = item.parentId, image = currentPhotoPath.getRealUri()))
            } else {
                if (item.isReplyPage) {
                    textRelay.onNext(
                            SendMessageData(channelId = item.channelId, parentId = item.parentId, image = currentPhotoPath.getRealUri()))
                } else {
                    textRelay.onNext(
                            SendMessageData(channelId = item.channelId, parentId = null, image = currentPhotoPath.getRealUri()))
                }
            }
        })
    }

    fun renderAttachSending(channelId: String, parentId: String? = null, uri: Uri? = null) {
        if (uri != null) currentPhotoPath = RealPathUtil.getRealPath(context, uri)
        send_image.visibility = View.VISIBLE
        send_image.setupView(currentPhotoPath, sent = {
            send_image.visibility = View.GONE
            textRelay.onNext(
                    SendMessageData(channelId = channelId, parentId = parentId, file = currentPhotoPath.getRealUri()))
        })
    }

    fun renderSelectFile() = send_message.attachMessage()

    fun renderReplying(item: EkoMessage, isReplyPage: Boolean) {
        replying_to.visibility = View.VISIBLE
        replying_to.setView(item)
        replyingStateRelay.onNext(ReplyingStateData(
                channelId = item.channelId,
                parentId = item.messageId,
                isNotCancel = true,
                isReplyPage = isReplyPage))
        replying_to.cancelReplying {
            hideReplying()
            if (isReplyPage) {
                replyingStateRelay.onNext(ReplyingStateData(
                        channelId = item.channelId,
                        parentId = item.parentId,
                        isNotCancel = false,
                        isReplyPage = isReplyPage))
            } else {
                replyingStateRelay.onNext(ReplyingStateData(
                        channelId = item.channelId,
                        parentId = null,
                        isNotCancel = false,
                        isReplyPage = isReplyPage))
            }
        }
    }

    private fun hideReplying() {
        replying_to.visibility = View.GONE
    }
}
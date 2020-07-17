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
import com.ekoapp.sample.chatfeature.data.MessageData
import com.ekoapp.sample.core.utils.getRealUri
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.component_main_send_message.view.*

class MainSendMessageComponent : ConstraintLayout {
    private val messageRelay = PublishProcessor.create<MessageData>()
    private var currentPath: String = ""

    fun message() = messageRelay

    init {
        LayoutInflater.from(context).inflate(R.layout.component_main_send_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun renderTextSending() {
        send_message.textMessage(sent = {
            hideReplying()
            messageRelay.onNext(MessageData(text = it))
        })
    }

    fun renderSelectPhoto(fm: FragmentManager) {
        send_message.imageMessage(fm) { currentPath = it }
    }

    fun renderImageSending(uri: Uri? = null) {
        if (uri != null) currentPath = RealPathUtil.getRealPath(context, uri)
        send_image.visibility = View.VISIBLE
        send_image.setupView(currentPath, sent = {
            send_image.visibility = View.GONE
            hideReplying()
            messageRelay.onNext(MessageData(image = currentPath.getRealUri()))
        })
    }

    fun renderSelectFile() {
        send_message.attachMessage()
    }

    fun renderAttachSending(uri: Uri? = null) {
        if (uri != null) currentPath = RealPathUtil.getRealPath(context, uri)
        send_image.visibility = View.VISIBLE
        send_image.setupView(currentPath, sent = {
            send_image.visibility = View.GONE
            hideReplying()
            messageRelay.onNext(MessageData(file = currentPath.getRealUri()))
        })
    }

    fun renderCustomSending(fm: FragmentManager) {
        send_message.customMessage(fm)
    }

    fun renderReplyingView(item: EkoMessage, replyingAction: (String?) -> Unit) {
        replying_to.visibility = View.VISIBLE
        replying_to.setView(item)
        replyingAction.invoke(item.messageId)
        replying_to.cancelReplying {
            hideReplying()
            replyingAction.invoke(null)
        }
    }

    private fun hideReplying() {
        replying_to.visibility = View.GONE
    }
}
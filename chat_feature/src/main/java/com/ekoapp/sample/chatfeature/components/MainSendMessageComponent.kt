package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.SendMessageData
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.component_main_send_message.view.*
import java.io.File

class MainSendMessageComponent : ConstraintLayout {
    private var currentPhotoPath: String = ""
    private var currentPhotoUri: Uri? = null

    companion object {
        var parentId: String? = null
    }

    private val textRelay = PublishProcessor.create<SendMessageData>()

    fun message() = textRelay

    init {
        LayoutInflater.from(context).inflate(R.layout.component_main_send_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun renderTextSending(channelId: String) {
        send_message.textMessage {
            hideReplying()
            textRelay.onNext(SendMessageData(channelId = channelId, parentId = parentId, text = it))
            parentId = null
        }
    }

    fun renderSelectPhoto(fm: FragmentManager) {
        send_message.imageMessage(fm) {
            currentPhotoPath = it
            val f = File(currentPhotoPath)
            val contentUri = Uri.fromFile(f)
            currentPhotoUri = contentUri
        }
    }

    fun renderImageSending(channelId: String) {
        send_image.visibility = View.VISIBLE
        send_image.setupView(currentPhotoPath) {
            send_image.visibility = View.GONE
            textRelay.onNext(SendMessageData(channelId = channelId, parentId = parentId, image = currentPhotoUri))
        }
    }

    fun renderReplying(item: EkoMessage) {
        parentId = item.messageId
        replying_to.visibility = View.VISIBLE
        replying_to.setView(item)
        replying_to.cancelReplying {
            parentId = null
            hideReplying()
        }
    }

    private fun hideReplying() {
        replying_to.visibility = View.GONE
    }
}
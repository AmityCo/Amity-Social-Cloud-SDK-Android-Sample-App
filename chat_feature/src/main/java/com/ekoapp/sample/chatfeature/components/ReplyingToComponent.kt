package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.messaging.data.ImageData
import com.ekoapp.ekosdk.messaging.data.TextData
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.messages.seals.MessageSealed
import com.ekoapp.sample.chatfeature.messages.view.list.renders.getMessageSealed
import kotlinx.android.synthetic.main.component_replying_to.view.*

class ReplyingToComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_replying_to, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setView(item: EkoMessage) {
        text_replying_title.text = context.getString(R.string.temporarily_replying_to, item.userId.getDisplayNameReplyTo())
        item.setMessage()
    }

    fun cancelReplying(action: () -> Unit) {
        image_cancel.setOnClickListener { action.invoke() }
    }

    private fun String.getDisplayNameReplyTo(): String {
        return if (this == EkoClient.getUserId()) context.getString(R.string.temporarily_yourself) else this
    }

    private fun EkoMessage.setMessage() {
        when (getMessageSealed()) {
            is MessageSealed.Text -> {
                image_replying.visibility = View.INVISIBLE
                text_replying_subtitle.text = getData(TextData::class.java).text
            }
            is MessageSealed.Image -> {
                image_replying.visibility = View.VISIBLE
                text_replying_subtitle.text = context.getString(R.string.temporarily_photo)
                Glide.with(context).load(getData(ImageData::class.java).url)
                        .placeholder(R.drawable.ic_placeholder_file)
                        .into(image_replying)
            }
            is MessageSealed.File -> {
                image_replying.visibility = View.INVISIBLE
                text_replying_subtitle.text = context.getString(R.string.temporarily_attachment)
            }
            is MessageSealed.Custom -> {
                image_replying.visibility = View.INVISIBLE
                text_replying_subtitle.text = data.toString()
            }
        }
    }

}
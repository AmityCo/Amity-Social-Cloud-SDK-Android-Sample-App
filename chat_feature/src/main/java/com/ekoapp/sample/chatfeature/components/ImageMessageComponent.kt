package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.messaging.data.ImageData
import com.ekoapp.sample.chatfeature.R
import kotlinx.android.synthetic.main.component_image_message.view.*

class ImageMessageComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_image_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setMessage(item: EkoMessage, action: (EkoMessage) -> Unit) {
        Glide.with(context).load(item.getData(ImageData::class.java).url)
                .placeholder(R.drawable.ic_placeholder_file)
                .into(image_message_content)

        image_message_content.setOnLongClickListener {
            image_reply.visibility = View.VISIBLE
            return@setOnLongClickListener true
        }
        image_reply.setOnClickListener {
            image_reply.visibility = View.INVISIBLE
            action.invoke(item)
        }
    }

    fun Boolean.showOrHideAvatar() {
        if (this) {
            avatar.visibility = View.INVISIBLE
        } else {
            avatar.visibility = View.VISIBLE
        }
    }
}
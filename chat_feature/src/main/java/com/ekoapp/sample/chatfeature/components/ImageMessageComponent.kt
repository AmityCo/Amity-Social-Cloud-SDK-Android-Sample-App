package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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

    fun setupView(item: EkoMessage) {
        Glide.with(context).load(item.getData(ImageData::class.java).url).into(image_message_content)
    }
}
package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.messaging.data.FileData
import com.ekoapp.sample.chatfeature.R
import kotlinx.android.synthetic.main.component_file_message.view.*

class FileMessageComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_file_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoMessage) {
        item.getData(FileData::class.java).apply {
            text_message_url.text = url
            setFileName()
            setCaption()
        }
    }

    private fun FileData.setFileName() {
        if (fileName.isNullOrEmpty()) {
            text_message_file_name.visibility = View.INVISIBLE
        } else {
            text_message_file_name.visibility = View.VISIBLE
            text_message_file_name.text = fileName
        }
    }

    private fun FileData.setCaption() {
        if (caption.isNullOrEmpty()) {
            text_message_caption.visibility = View.GONE
        } else {
            text_message_caption.visibility = View.VISIBLE
            text_message_caption.text = caption
        }
    }
}
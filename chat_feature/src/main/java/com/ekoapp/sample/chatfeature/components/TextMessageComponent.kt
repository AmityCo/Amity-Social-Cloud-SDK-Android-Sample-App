package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.messaging.data.TextData
import com.ekoapp.sample.chatfeature.R
import kotlinx.android.synthetic.main.component_text_message.view.*

class TextMessageComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_text_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setMessage(item: EkoMessage, action: (EkoMessage) -> Unit) {
        text_message_content.text = item.getData(TextData::class.java).text
        text_message_content.setOnLongClickListener {
            action.invoke(item)
            return@setOnLongClickListener true
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
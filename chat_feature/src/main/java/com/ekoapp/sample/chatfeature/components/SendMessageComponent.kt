package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.chatfeature.R
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.component_send_message.view.*

class SendMessageComponent : ConstraintLayout {

    private var textRelay = PublishProcessor.create<String>()

    fun text() = textRelay

    init {
        LayoutInflater.from(context).inflate(R.layout.component_send_message, this, true)
        setupEvent()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private fun setupEvent() {
        edit_text_message.clearComposingText()
        edit_text_message.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                edit_text_message.removeTextChangedListener(this)
                text.setSendButton()
                edit_text_message.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

        })

        image_send.setOnClickListener {
            textRelay.onNext(edit_text_message.text.toString())
            edit_text_message.setText("")
            edit_text_message.clearComposingText()
        }
    }

    private fun Editable?.setSendButton() {
        if (!this?.trim().isNullOrEmpty()) {
            image_send.visibility = View.VISIBLE
        } else {
            image_send.visibility = View.GONE
        }
    }
}
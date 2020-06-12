package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.R
import kotlinx.android.synthetic.main.component_main_send_message.view.*

class MainSendMessageComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_main_send_message, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun messageRelay() = send_message.text()

    fun replyingRelay(item: EkoMessage) {
        replying_to.visibility = View.VISIBLE
        replying_to.setView(item)
        replying_to.cancelReplying { replying_to.visibility = View.GONE }
    }
}
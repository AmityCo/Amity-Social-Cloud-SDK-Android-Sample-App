package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.constants.ZERO_COUNT
import kotlinx.android.synthetic.main.component_avatar_with_total.view.*

class AvatarChatRoomWithTotalComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_avatar_chat_room_with_total, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(total: Int) {
        if (total > ZERO_COUNT) {
            button_total.visibility = View.VISIBLE
            button_total.text = total.toString()
        } else {
            button_total.visibility = View.GONE
        }
    }
}
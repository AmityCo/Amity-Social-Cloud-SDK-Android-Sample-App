package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.chatfeature.R
import kotlinx.android.synthetic.main.component_header_channels.view.*

class HeaderChannelsComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_header_channels, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(total: Int) {
        avatar_with_total.setupView(total)
    }
}
package com.ekoapp.sample.socialfeature.userfeed.view.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.component_header_create_feeds.view.*


class HeaderCreateFeedsComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_header_create_feeds, this, true)
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private fun setupView() {
        text_full_name.text = EkoClient.getUserId()
    }
}
package com.ekoapp.sample.socialfeature.userfeed.view.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import kotlinx.android.synthetic.main.component_body_feeds.view.*


class BodyFeedsComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_body_feeds, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(data: SampleFeedsResponse) {
        text_description.text = data.description
    }

}
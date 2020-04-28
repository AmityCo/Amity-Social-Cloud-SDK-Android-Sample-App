package com.ekoapp.sample.socialfeature.userfeed.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.socialfeature.R


class CommentActionComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_comment_action, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    )

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
}
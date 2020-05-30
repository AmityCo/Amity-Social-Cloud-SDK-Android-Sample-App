package com.ekoapp.sample.socialfeature.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ekoapp.sample.core.utils.setTint
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.component_like_action.view.*


class LikeActionComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_like_action, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    @SuppressLint("PrivateResource")
    fun likeView() {
        image_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like))
        image_like.setTint(ContextCompat.getColor(context, R.color.colorLike))
        text_like.setTextColor(ContextCompat.getColor(context, R.color.colorLike))
    }

    fun likedView() {
        image_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_liked))
        image_like.setTint(ContextCompat.getColor(context, R.color.colorLiked))
        text_like.setTextColor(ContextCompat.getColor(context, R.color.colorLiked))
    }
}
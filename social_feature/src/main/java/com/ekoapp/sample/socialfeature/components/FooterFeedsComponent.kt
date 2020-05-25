package com.ekoapp.sample.socialfeature.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.utils.setTint
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import kotlinx.android.synthetic.main.component_footer_feeds.view.*
import kotlinx.android.synthetic.main.component_like_action.view.*


class FooterFeedsComponent : ConstraintLayout {

    private var isLiked = false

    init {
        LayoutInflater.from(context).inflate(R.layout.component_footer_feeds, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoPost) {
        renderLike(item)
    }

    private fun renderLike(item: EkoPost) {
        when (item.reactions[ReactionTypes.LIKE.text]) {
            0 -> {
                isLiked = false
                selectorLike(isLiked)
            }
            null -> {
                isLiked = false
                selectorLike(isLiked)
            }
            else -> {
                isLiked = true
                selectorLike(isLiked)
            }
        }
    }

    fun selectorLike(isLike: Boolean) = if (isLike) likedView() else likeView()

    fun likeFeeds(actionLike: (Boolean) -> Unit) {
        like_action.setOnClickListener {
            actionLike.invoke(!isLiked)
        }
    }

    @SuppressLint("PrivateResource")
    private fun likeView() {
        like_action.image_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like))
        like_action.image_like.setTint(ContextCompat.getColor(context, R.color.colorDisable))
        like_action.text_like.setTextColor(ContextCompat.getColor(context, R.color.colorDisable))
    }

    private fun likedView() {
        like_action.image_like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_liked))
        like_action.image_like.setTint(ContextCompat.getColor(context, R.color.colorEnable))
        like_action.text_like.setTextColor(ContextCompat.getColor(context, R.color.colorEnable))
    }
}
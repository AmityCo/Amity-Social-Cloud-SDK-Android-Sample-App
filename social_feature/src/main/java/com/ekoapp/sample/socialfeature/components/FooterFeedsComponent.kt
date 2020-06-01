package com.ekoapp.sample.socialfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import kotlinx.android.synthetic.main.component_footer_feeds.view.*


class FooterFeedsComponent : ConstraintLayout {

    private var isLiked = false

    init {
        LayoutInflater.from(context).inflate(R.layout.component_footer_feeds, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoPost) {

        //TODO Use get my reaction from value view model instead
        val match = item.myReactions.filter { ReactionTypes.LIKE.text.contains(it, ignoreCase = true) }
        isLiked = match.contains(ReactionTypes.LIKE.text)
        selectorLike(isLiked)
    }

    private fun selectorLike(isLike: Boolean) = if (isLike) like_action.likedView() else like_action.likeView()

    fun likeFeeds(actionLike: (Boolean) -> Unit) {
        like_action.setOnClickListener {
            actionLike.invoke(!isLiked)
        }
    }
}
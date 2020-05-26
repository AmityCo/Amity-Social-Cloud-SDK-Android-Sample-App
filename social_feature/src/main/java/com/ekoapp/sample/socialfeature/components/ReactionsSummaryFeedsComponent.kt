package com.ekoapp.sample.socialfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.component_reactions_summary_feeds.view.*


class ReactionsSummaryFeedsComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_reactions_summary_feeds, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoPost) {
        text_total_reactions.text = item.reactionCount.toString()
    }
}
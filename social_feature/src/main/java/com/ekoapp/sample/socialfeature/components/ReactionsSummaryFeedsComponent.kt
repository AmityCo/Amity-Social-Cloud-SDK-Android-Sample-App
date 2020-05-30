package com.ekoapp.sample.socialfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.base.list.DOUBLE_SPACE
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.list.SINGLE_SPACE
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.ZERO_COUNT
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import com.ekoapp.sample.socialfeature.userfeeds.view.list.ReactionsAdapter
import kotlinx.android.synthetic.main.component_reactions_summary_feeds.view.*


class ReactionsSummaryFeedsComponent : LinearLayout {
    private lateinit var adapter: ReactionsAdapter

    init {
        LayoutInflater.from(context).inflate(R.layout.component_reactions_summary_feeds, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoPost) {
        val totalLike = item.reactions.getCount(ReactionTypes.LIKE.text)
        val totalFavorite = item.reactions.getCount(ReactionTypes.FAVORITE.text)
        val totalReactions = totalLike + totalFavorite
        text_total_reactions.text = totalReactions.toString()
        renderList(totalLike, totalFavorite)
    }

    private fun renderList(totalLike: Int, totalFavorite: Int) {
        val reactions = ArrayList<Int>()
        if (totalLike > ZERO_COUNT) {
            reactions.add(R.drawable.ic_see_like)
        }
        if (totalFavorite > ZERO_COUNT) {
            reactions.add(R.drawable.ic_see_favorite)
        }

        val customSpaceCount: Int = setSpaceCount(totalLike, totalFavorite)
        adapter = ReactionsAdapter(context = context, items = reactions)
        RecyclerBuilder(context = context, recyclerView = recycler_reactions, spaceCount = customSpaceCount)
                .builder()
                .build(adapter)
    }

    private fun setSpaceCount(totalLike: Int, totalFavorite: Int): Int {
        return if (totalLike > ZERO_COUNT && totalFavorite > ZERO_COUNT) {
            DOUBLE_SPACE
        } else {
            SINGLE_SPACE
        }
    }
}
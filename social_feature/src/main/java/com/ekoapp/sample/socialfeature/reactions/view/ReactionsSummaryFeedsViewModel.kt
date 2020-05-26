package com.ekoapp.sample.socialfeature.reactions.view

import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.R
import javax.inject.Inject

data class TabLayoutData(val icon: Int, val title: Int)
class ReactionsSummaryFeedsViewModel @Inject constructor() : DisposableViewModel() {
    private val pageLike = 0
    private val pageFavorite = 1

    fun getTabLayout(position: Int): TabLayoutData {
        return when (position) {
            pageLike -> {
                TabLayoutData(icon = R.drawable.ic_see_like, title = R.string.temporarily_like)
            }
            pageFavorite -> {
                TabLayoutData(icon = R.drawable.ic_see_favorite, title = R.string.temporarily_favorite)
            }
            else -> {
                TabLayoutData(icon = R.drawable.ic_see_like, title = R.string.temporarily_like)
            }
        }
    }
}
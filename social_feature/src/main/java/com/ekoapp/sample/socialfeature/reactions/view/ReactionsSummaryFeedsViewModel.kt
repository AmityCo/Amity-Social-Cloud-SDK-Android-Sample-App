package com.ekoapp.sample.socialfeature.reactions.view

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.internal.data.model.EkoPostReaction
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import javax.inject.Inject

data class TabLayoutData(val icon: Int, val title: Int)

class ReactionsSummaryFeedsViewModel @Inject constructor() : DisposableViewModel() {
    private var userReactionIntent: UserReactionData? = null
    private val pageLike = 0
    private val pageFavorite = 1

    fun getIntentUserData(actionRelay: (UserReactionData) -> Unit) {
        userReactionIntent?.let(actionRelay::invoke)
    }

    fun setupIntent(data: UserReactionData?) {
        userReactionIntent = data
    }

    fun getPostReactionList(postId: String, reactionName: String): LiveData<PagedList<EkoPostReaction>> {
        return EkoClient.newFeedRepository().getPostReactionCollectionByReactionName(postId, reactionName)
    }

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
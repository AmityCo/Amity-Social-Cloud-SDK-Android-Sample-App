package com.ekoapp.sample.socialfeature.reactions.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.internal.data.model.EkoPostReaction
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.ZERO_COUNT
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.repositories.FeedRepository
import javax.inject.Inject

data class TabLayoutData(val icon: Int, val title: Int, val total: Int)

class ReactionsSummaryFeedsViewModel @Inject constructor(private val feedRepository: FeedRepository) : DisposableViewModel() {
    private var userReactionIntent: UserReactionData? = null
    private val pageLike = 0
    private val pageFavorite = 1

    fun getIntentUserData(actionRelay: (UserReactionData) -> Unit) {
        userReactionIntent?.let(actionRelay::invoke)
    }

    fun setupIntent(data: UserReactionData?) {
        userReactionIntent = data
    }

    fun bindPostReactions(postId: String): LiveData<PagedList<EkoPostReaction>> {
        return feedRepository.getPostReactionCollection(postId)
    }

    fun bindPostReactionsByName(postId: String, reactionName: String): LiveData<PagedList<EkoPostReaction>> {
        return feedRepository.getPostReactionCollectionByReactionName(postId, reactionName)
    }

    fun getTabLayout(totalLike: Int = ZERO_COUNT, totalFavorite: Int = ZERO_COUNT, position: Int): TabLayoutData {
        return when (position) {
            pageLike -> {
                TabLayoutData(icon = R.drawable.ic_see_like, title = R.string.temporarily_total_like, total = totalLike)
            }
            pageFavorite -> {
                TabLayoutData(icon = R.drawable.ic_see_favorite, title = R.string.temporarily_total_favorite, total = totalFavorite)
            }
            else -> {
                TabLayoutData(icon = R.drawable.ic_see_like, title = R.string.temporarily_like, total = totalLike)
            }
        }
    }

    fun getTotal(items: List<EkoPostReaction>, reactionTypes: ReactionTypes): Int {
        return items.filter { item -> item.reactionName == reactionTypes.text }.size
    }

    fun getFragments(postId: String): ArrayList<Fragment> {
        val fragments = ArrayList<Fragment>()
        fragments.add(UserReactionFeedsFragment.newInstance(UserReactionData(postId = postId, reactionTypes = ReactionTypes.LIKE)))
        fragments.add(UserReactionFeedsFragment.newInstance(UserReactionData(postId = postId, reactionTypes = ReactionTypes.FAVORITE)))
        return fragments
    }
}
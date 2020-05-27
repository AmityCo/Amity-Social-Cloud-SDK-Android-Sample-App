package com.ekoapp.sample.socialfeature.reactions.view.list


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.reactions.view.UserReactionFeedsFragment

class ReactionsSummaryFeedsAdapter(val item: UserReactionData, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val reactionPages = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> UserReactionFeedsFragment.newInstance(UserReactionData(postId = item.postId, reactionTypes = ReactionTypes.LIKE))
        1 -> UserReactionFeedsFragment.newInstance(UserReactionData(postId = item.postId, reactionTypes = ReactionTypes.FAVORITE))
        else -> UserReactionFeedsFragment.newInstance(UserReactionData(postId = item.postId, reactionTypes = ReactionTypes.LIKE))
    }

    override fun getItemCount(): Int = reactionPages
}


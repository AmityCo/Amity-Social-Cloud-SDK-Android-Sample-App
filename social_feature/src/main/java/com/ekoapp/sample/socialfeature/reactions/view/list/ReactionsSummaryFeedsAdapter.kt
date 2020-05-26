package com.ekoapp.sample.socialfeature.reactions.view.list


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.reactions.view.UserReactionFavoriteFeedsFragment
import com.ekoapp.sample.socialfeature.reactions.view.UserReactionLikeFeedsFragment

class ReactionsSummaryFeedsAdapter(val item: UserReactionData, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val reactionPages = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> UserReactionLikeFeedsFragment(item = item)
        1 -> UserReactionFavoriteFeedsFragment(item = item)
        else -> UserReactionLikeFeedsFragment(item = item)
    }

    override fun getItemCount(): Int = reactionPages
}


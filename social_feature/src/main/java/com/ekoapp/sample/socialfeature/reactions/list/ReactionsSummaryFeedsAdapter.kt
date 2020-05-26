package com.ekoapp.sample.socialfeature.reactions.list


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ekoapp.sample.socialfeature.reactions.view.UserReactionFavoriteFeedsFragment
import com.ekoapp.sample.socialfeature.reactions.view.UserReactionLikeFeedsFragment

class ReactionsSummaryFeedsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val reactionPages = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> UserReactionLikeFeedsFragment()
        1 -> UserReactionFavoriteFeedsFragment()
        else -> UserReactionLikeFeedsFragment()
    }

    override fun getItemCount(): Int = reactionPages
}


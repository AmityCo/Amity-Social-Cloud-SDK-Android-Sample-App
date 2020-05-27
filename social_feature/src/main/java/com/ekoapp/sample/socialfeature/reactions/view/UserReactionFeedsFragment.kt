package com.ekoapp.sample.socialfeature.reactions.view

import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.di.DaggerSocialFragmentComponent
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.reactions.view.list.EkoUserReactionFeedsAdapter
import kotlinx.android.synthetic.main.fragment_user_reactions.*

class UserReactionFeedsFragment(val item: UserReactionData) : SingleViewModelFragment<ReactionsSummaryFeedsViewModel>() {
    private val spaceUsers = 1
    lateinit var adapter: EkoUserReactionFeedsAdapter

    companion object {
        fun newInstance(item: UserReactionData) = UserReactionFeedsFragment(item = item)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_user_reactions
    }

    override fun bindViewModel(viewModel: ReactionsSummaryFeedsViewModel) {
        renderList(viewModel)
    }

    private fun renderList(viewModel: ReactionsSummaryFeedsViewModel) {
        adapter = EkoUserReactionFeedsAdapter(requireContext())
        RecyclerBuilder(context = requireContext(), recyclerView = recycler_user_reactions, spaceCount = spaceUsers)
                .builder()
                .buildMultiView(adapter)
        viewModel.getPostReactionListByName(item.postId, item.reactionTypes.text).observeNotNull(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    override fun getViewModelClass(): Class<ReactionsSummaryFeedsViewModel> {
        return ReactionsSummaryFeedsViewModel::class.java
    }

    override fun initDependencyInjection() {
        DaggerSocialFragmentComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}
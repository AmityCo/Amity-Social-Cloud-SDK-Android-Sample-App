package com.ekoapp.sample.socialfeature.reactions.view

import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.di.DaggerSocialFragmentComponent

class UserReactionLikeFeedsFragment : SingleViewModelFragment<ReactionsSummaryFeedsViewModel>() {
    override fun getLayout(): Int {
        return R.layout.fragment_user_reactions
    }

    override fun bindViewModel(viewModel: ReactionsSummaryFeedsViewModel) {

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
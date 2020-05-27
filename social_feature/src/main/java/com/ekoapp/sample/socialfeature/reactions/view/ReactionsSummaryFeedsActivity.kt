package com.ekoapp.sample.socialfeature.reactions.view

import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_USER_REACTION_DATA
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.reactions.view.list.ReactionsSummaryFeedsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_reactions_summary_feeds.*

class ReactionsSummaryFeedsActivity : SingleViewModelActivity<ReactionsSummaryFeedsViewModel>() {

    override fun bindViewModel(viewModel: ReactionsSummaryFeedsViewModel) {
        val item = intent.extras?.getParcelable<UserReactionData>(EXTRA_USER_REACTION_DATA)
        viewModel.setupIntent(item)
        setupAppBar()
        setupView(viewModel)
    }

    private fun setupView(viewModel: ReactionsSummaryFeedsViewModel) {
        view_pager_reactions.offscreenPageLimit = 1
        viewModel.getIntentUserData {
            initTabLayout(viewModel, it)
            val adapter = ReactionsSummaryFeedsAdapter(it, supportFragmentManager, lifecycle)
            view_pager_reactions.adapter = adapter
        }
    }

    private fun setupAppBar() {
        appbar_reactions_summary_feeds.setup(this, true)
        appbar_reactions_summary_feeds.setTitle(getString(R.string.temporarily_app_bar_reactions_summary))
    }

    private fun initTabLayout(viewModel: ReactionsSummaryFeedsViewModel, it: UserReactionData) {
        viewModel.getPostReactionList(it.postId).observeNotNull(this, { items ->
            val totalLike = viewModel.getTotal(items, ReactionTypes.LIKE)
            val totalFavorite = viewModel.getTotal(items, ReactionTypes.FAVORITE)
            TabLayoutMediator(tab_layout, view_pager_reactions) { tab, position ->
                viewModel.getTabLayout(totalLike = totalLike, totalFavorite = totalFavorite, position = position).apply {
                    tab.text = String.format(getString(title), total)
                }
            }.attach()
        })
    }

    override fun getViewModelClass(): Class<ReactionsSummaryFeedsViewModel> {
        return ReactionsSummaryFeedsViewModel::class.java
    }

    override fun getLayout(): Int {
        return R.layout.activity_reactions_summary_feeds
    }

    override fun initDependencyInjection() {
        DaggerSocialActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}
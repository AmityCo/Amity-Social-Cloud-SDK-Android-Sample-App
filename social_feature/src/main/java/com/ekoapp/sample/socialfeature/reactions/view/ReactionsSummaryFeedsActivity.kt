package com.ekoapp.sample.socialfeature.reactions.view

import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.reactions.list.ReactionsSummaryFeedsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_reactions_summary_feeds.*

class ReactionsSummaryFeedsActivity : SingleViewModelActivity<ReactionsSummaryFeedsViewModel>() {

    override fun bindViewModel(viewModel: ReactionsSummaryFeedsViewModel) {
        setupView(viewModel)
    }

    private fun setupView(viewModel: ReactionsSummaryFeedsViewModel) {
        val adapter = ReactionsSummaryFeedsAdapter(supportFragmentManager, lifecycle)
        view_pager_reactions.adapter = adapter

        TabLayoutMediator(tab_layout, view_pager_reactions) { tab, position ->
            viewModel.getTabLayout(position).apply {
//                tab.icon = ContextCompat.getDrawable(this@ReactionsSummaryFeedsActivity, icon)
                tab.text = getString(title)
            }
        }.attach()
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
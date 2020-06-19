package com.ekoapp.sample.socialfeature.userfeeds.view

import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_EKO_POST
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.intents.openEditFeedsPage
import com.ekoapp.sample.socialfeature.intents.openReactionsSummaryFeedsPage
import com.ekoapp.sample.socialfeature.intents.openUserFeedsPage
import com.ekoapp.sample.socialfeature.userfeeds.data.FeedsData
import kotlinx.android.synthetic.main.activity_body_user_feeds.*


class BodyUserFeedsActivity : SingleViewModelActivity<UserFeedsViewModel>() {

    override fun getLayout(): Int {
        return R.layout.activity_body_user_feeds
    }

    override fun bindViewModel(viewModel: UserFeedsViewModel) {
        val item = intent.extras?.getParcelable<FeedsData>(EXTRA_EKO_POST)
        viewModel.setupIntent(item)
        setupAppBar(viewModel)
        bindUserFeeds(viewModel)
        setupEvent(viewModel)
    }

    private fun setupAppBar(viewModel: UserFeedsViewModel) {
        appbar_body_user_feeds.setup(this, true)
        viewModel.getIntentFeedsData {
            appbar_body_user_feeds.setTitle(it.postId)
        }
    }

    private fun bindUserFeeds(viewModel: UserFeedsViewModel) {
        viewModel.getIntentFeedsData {
            viewModel.bindGetPost(it).observeNotNull(this, { item ->
                feeds.setupView(item, viewModel)
            })
        }
    }

    private fun setupEvent(viewModel: UserFeedsViewModel) {
        viewModel.observeReactionsSummaryPage().observeNotNull(this, this::openReactionsSummaryFeedsPage)
        viewModel.observeUserPage().observeNotNull(this, this::openUserFeedsPage)
        viewModel.observeEditFeedsPage().observeNotNull(this, this::openEditFeedsPage)
        viewModel.observeDeleteFeeds().observeNotNull(this, {
            onBackPressed()
        })
    }

    override fun initDependencyInjection() {
        DaggerSocialActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

    override fun getViewModelClass(): Class<UserFeedsViewModel> {
        return UserFeedsViewModel::class.java
    }
}
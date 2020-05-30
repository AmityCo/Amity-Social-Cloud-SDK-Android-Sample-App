package com.ekoapp.sample.socialfeature.userfeeds.view

import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_EKO_POST
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
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
    }

    private fun bindUserFeeds(viewModel: UserFeedsViewModel) {
        viewModel.getIntentFeedsData {

        }
    }

    private fun setupAppBar(viewModel: UserFeedsViewModel) {
        appbar_body_user_feeds.setup(this, true)
        viewModel.getIntentFeedsData {

        }
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
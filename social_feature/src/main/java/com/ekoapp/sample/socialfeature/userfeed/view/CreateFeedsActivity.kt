package com.ekoapp.sample.socialfeature.userfeed.view

import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.di.DaggerSocialActivityComponent
import kotlinx.android.synthetic.main.activity_create_feeds.*
import timber.log.Timber

class CreateFeedsActivity : SingleViewModelActivity<CreateFeedsViewModel>() {

    override fun bindViewModel(viewModel: CreateFeedsViewModel) {
        setupAppBar()
    }

    private fun setupAppBar() {
        appbar_create_feeds.setup(this, true)
        appbar_create_feeds.setTitle(getString(R.string.temporarily_create_post))
    }

    override fun getViewModelClass(): Class<CreateFeedsViewModel> {
        return CreateFeedsViewModel::class.java
    }

    override fun getLayout(): Int {
        return R.layout.activity_create_feeds
    }

    override fun initDependencyInjection() {
        DaggerSocialActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

}
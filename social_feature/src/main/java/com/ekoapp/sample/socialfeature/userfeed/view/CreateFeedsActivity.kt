package com.ekoapp.sample.socialfeature.userfeed.view

import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.di.DaggerSocialActivityComponent
import timber.log.Timber

class CreateFeedsActivity : SingleViewModelActivity<CreateFeedsViewModel>() {

    override fun bindViewModel(viewModel: CreateFeedsViewModel) {
        Timber.d(getCurrentClassAndMethodNames())
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
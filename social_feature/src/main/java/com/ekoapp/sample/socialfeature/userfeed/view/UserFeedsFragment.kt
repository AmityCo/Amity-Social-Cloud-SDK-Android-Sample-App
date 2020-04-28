package com.ekoapp.sample.socialfeature.userfeed.view

import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.di.DaggerSocialFragmentComponent
import timber.log.Timber

class UserFeedsFragment : SingleViewModelFragment<UserFeedsViewModel>() {

    override fun getLayout(): Int {
        return R.layout.fragment_user_feeds
    }

    override fun bindViewModel(viewModel: UserFeedsViewModel) {
        Timber.d(getCurrentClassAndMethodNames())

    }

    override fun initDependencyInjection() {
        DaggerSocialFragmentComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

    override fun getViewModelClass(): Class<UserFeedsViewModel> {
        return UserFeedsViewModel::class.java
    }
}
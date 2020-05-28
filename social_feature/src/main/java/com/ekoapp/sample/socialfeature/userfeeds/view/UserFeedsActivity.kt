package com.ekoapp.sample.socialfeature.userfeeds.view

import android.os.Bundle
import com.ekoapp.sample.core.base.addFragment
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_USER_DATA
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.activity_user_feeds.*


class UserFeedsActivity : SingleViewModelActivity<UserFeedsViewModel>() {

    override fun getLayout(): Int {
        return R.layout.activity_user_feeds
    }

    override fun bindViewModel(viewModel: UserFeedsViewModel) {
        val item = intent.extras?.getParcelable<UserData>(EXTRA_USER_DATA)
        viewModel.setupIntent(item)
        setupAppBar(viewModel)
        bindUserFeeds(viewModel)
    }

    private fun bindUserFeeds(viewModel: UserFeedsViewModel) {
        viewModel.getIntentUserData {
            val fragment = UserFeedsFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_USER_DATA, it)
            fragment.arguments = args
            addFragment(R.id.main_content_frame, fragment)
        }
    }

    private fun setupAppBar(viewModel: UserFeedsViewModel) {
        appbar_user_feeds.setup(this, true)
        viewModel.getIntentUserData {
            appbar_user_feeds.setTitle(it.userId)
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
package com.ekoapp.sample.socialfeature.users.view

import com.ekoapp.sample.core.base.addFragment
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_USER_DATA
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.activity_see_all_users.*

class SeeAllUsersActivity : SingleViewModelActivity<UsersViewModel>() {

    override fun getLayout(): Int {
        return R.layout.activity_see_all_users
    }

    override fun bindViewModel(viewModel: UsersViewModel) {
        val item = intent.extras?.getParcelable<UserData>(EXTRA_USER_DATA)
        viewModel.setupIntent(item)
        setupAppBar(viewModel)
        addFragment(R.id.main_content_frame, UsersFragment())
    }

    private fun setupAppBar(viewModel: UsersViewModel) {
        appbar_see_all_users.setup(this, true)
        viewModel.getIntentUserData {
            appbar_see_all_users.setTitle(it.userId)
        }
    }

    override fun getViewModelClass(): Class<UsersViewModel> {
        return UsersViewModel::class.java
    }

    override fun initDependencyInjection() {
        DaggerSocialActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}

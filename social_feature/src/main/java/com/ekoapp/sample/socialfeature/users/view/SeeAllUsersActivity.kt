package com.ekoapp.sample.socialfeature.users.view

import android.content.Intent
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_USER_DATA
import com.ekoapp.sample.socialfeature.constants.REQUEST_CODE_USER_FEEDS
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsActivity
import com.ekoapp.sample.socialfeature.users.data.UserData
import com.ekoapp.sample.socialfeature.users.view.list.EkoUsersAdapter
import kotlinx.android.synthetic.main.activity_see_all_users.*
import kotlinx.android.synthetic.main.fragment_users.header_users
import kotlinx.android.synthetic.main.fragment_users.recycler_users

class SeeAllUsersActivity : SingleViewModelActivity<UsersViewModel>() {
    private val spaceUsers = 1
    private lateinit var adapter: EkoUsersAdapter

    override fun getLayout(): Int {
        return R.layout.activity_see_all_users
    }

    override fun bindViewModel(viewModel: UsersViewModel) {
        val item = intent.extras?.getParcelable<UserData>(EXTRA_USER_DATA)
        viewModel.setupIntent(item)
        setupAppBar(viewModel)
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun setupAppBar(viewModel: UsersViewModel) {
        appbar_see_all_users.setup(this, true)
        viewModel.getIntentUserData {
            appbar_see_all_users.setTitle(it.userId)
        }
    }

    private fun renderList(viewModel: UsersViewModel) {
        adapter = EkoUsersAdapter(viewModel)
        RecyclerBuilder(context = this, recyclerView = recycler_users, spaceCount = spaceUsers)
                .builder()
                .build(adapter)
        viewModel.getUserList().observeNotNull(this, {
            header_users.setupView(it)
            adapter.submitList(it)
        })
    }

    private fun setupEvent(viewModel: UsersViewModel) {
        viewModel.observeUserPage().observeNotNull(this, {
            val intent = Intent(this, UserFeedsActivity::class.java)
            intent.putExtra(EXTRA_USER_DATA, it)
            startActivityForResult(intent, REQUEST_CODE_USER_FEEDS)
        })
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

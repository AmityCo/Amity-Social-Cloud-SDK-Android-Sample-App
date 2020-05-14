package com.ekoapp.sample.socialfeature.search

import android.content.Intent
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_USER_DATA
import com.ekoapp.sample.socialfeature.constants.REQUEST_CODE_USER_FEEDS
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsActivity
import com.ekoapp.sample.socialfeature.users.list.EkoUsersAdapter
import com.ekoapp.sample.socialfeature.users.view.UsersViewModel
import kotlinx.android.synthetic.main.activity_search_users.*
import kotlinx.android.synthetic.main.fragment_users.recycler_users

class SearchUsersActivity : SingleViewModelActivity<UsersViewModel>() {
    private val spaceUsers = 1
    private lateinit var adapter: EkoUsersAdapter

    override fun getLayout(): Int {
        return R.layout.activity_search_users
    }

    override fun bindViewModel(viewModel: UsersViewModel) {
        setupAppBar()
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun setupAppBar() {
        appbar_search.setup(this, true)
    }

    private fun renderList(viewModel: UsersViewModel) {
        adapter = EkoUsersAdapter(viewModel)
        RecyclerBuilder(context = this, recyclerView = recycler_users, spaceCount = spaceUsers)
                .builder()
                .build(adapter)
        viewModel.getUserList().observeNotNull(this, {
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

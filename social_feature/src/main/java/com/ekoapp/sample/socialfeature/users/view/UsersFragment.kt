package com.ekoapp.sample.socialfeature.users.view

import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.di.DaggerSocialFragmentComponent
import com.ekoapp.sample.socialfeature.intents.openUserFeedsPage
import com.ekoapp.sample.socialfeature.users.view.list.EkoUsersAdapter
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : SingleViewModelFragment<UsersViewModel>() {
    private lateinit var adapter: EkoUsersAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_users
    }

    override fun bindViewModel(viewModel: UsersViewModel) {
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun renderList(viewModel: UsersViewModel) {
        adapter = EkoUsersAdapter(requireContext(), viewModel)
        RecyclerBuilder(context = requireContext(), recyclerView = recycler_users)
                .builder()
                .build(adapter)
        viewModel.bindUserList().observeNotNull(viewLifecycleOwner, {
            header_users.setupView(it)
            adapter.submitList(it)
        })
    }

    private fun setupEvent(viewModel: UsersViewModel) {
        viewModel.observeUserPage().observeNotNull(viewLifecycleOwner, this::openUserFeedsPage)
    }

    override fun getViewModelClass(): Class<UsersViewModel> {
        return UsersViewModel::class.java
    }

    override fun initDependencyInjection() {
        DaggerSocialFragmentComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}

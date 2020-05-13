package com.ekoapp.sample.socialfeature.users.view

import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.di.DaggerSocialFragmentComponent
import com.ekoapp.sample.socialfeature.users.list.EkoUsersAdapter
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : SingleViewModelFragment<UsersViewModel>() {
    private val spaceUsers = 1
    private lateinit var adapter: EkoUsersAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_users
    }

    override fun bindViewModel(viewModel: UsersViewModel) {
        renderList(viewModel)
    }

    private fun renderList(viewModel: UsersViewModel) {
        adapter = EkoUsersAdapter()
        RecyclerBuilder(context = requireContext(), recyclerView = recycler_users, spaceCount = spaceUsers)
                .builder()
                .build(adapter)
        viewModel.getUserList().observeNotNull(viewLifecycleOwner, adapter::submitList)
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

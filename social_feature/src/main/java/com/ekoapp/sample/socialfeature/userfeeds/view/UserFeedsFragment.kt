package com.ekoapp.sample.socialfeature.userfeeds.view

import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_USER_DATA
import com.ekoapp.sample.socialfeature.di.DaggerSocialFragmentComponent
import com.ekoapp.sample.socialfeature.intents.*
import com.ekoapp.sample.socialfeature.userfeeds.view.list.MainUserFeedsAdapter
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.fragment_user_feeds.*

class UserFeedsFragment : SingleViewModelFragment<UserFeedsViewModel>() {

    private lateinit var adapter: MainUserFeedsAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_user_feeds
    }

    override fun bindViewModel(viewModel: UserFeedsViewModel) {
        val item = activity?.intent?.extras?.getParcelable<UserData>(EXTRA_USER_DATA)
        viewModel.setupIntent(item)
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun setupEvent(viewModel: UserFeedsViewModel) {
        viewModel.observeCreateFeedsPage().observeNotNull(viewLifecycleOwner, this::openCreateFeedsPage)
        viewModel.observeEditFeedsPage().observeNotNull(viewLifecycleOwner, this::openEditFeedsPage)
        viewModel.observeFindUsersPage().observeNotNull(viewLifecycleOwner, { this.openSearchUsersPage() })
        viewModel.observeSeeAllUsersPage().observeNotNull(viewLifecycleOwner, {
            viewModel.getIntentUserData(this::openSeeAllUsersPage)
        })
        viewModel.observeReactionsSummaryPage().observeNotNull(viewLifecycleOwner, this::openReactionsSummaryFeedsPage)
        viewModel.observeUserPage().observeNotNull(viewLifecycleOwner, this::openUserFeedsPage)
        viewModel.observeFeedsByIdPage().observeNotNull(viewLifecycleOwner, this::openFeedsById)
    }

    private fun renderList(viewModel: UserFeedsViewModel) {
        adapter = MainUserFeedsAdapter(context = requireContext(),
                lifecycleOwner = viewLifecycleOwner,
                viewModel = viewModel)

        RecyclerBuilder(context = requireContext(), recyclerView = recycler_main_feeds)
                .builder()
                .build(adapter)
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
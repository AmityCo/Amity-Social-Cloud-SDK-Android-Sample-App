package com.ekoapp.sample.socialfeature.userfeeds.view

import android.content.Intent
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.*
import com.ekoapp.sample.socialfeature.createfeeds.CreateFeedsActivity
import com.ekoapp.sample.socialfeature.di.DaggerSocialFragmentComponent
import com.ekoapp.sample.socialfeature.editfeeds.EditFeedsActivity
import com.ekoapp.sample.socialfeature.reactions.view.ReactionsSummaryFeedsActivity
import com.ekoapp.sample.socialfeature.search.SearchUsersActivity
import com.ekoapp.sample.socialfeature.userfeeds.view.list.MainUserFeedsAdapter
import com.ekoapp.sample.socialfeature.users.data.UserData
import com.ekoapp.sample.socialfeature.users.view.SeeAllUsersActivity
import kotlinx.android.synthetic.main.fragment_user_feeds.*

class UserFeedsFragment : SingleViewModelFragment<UserFeedsViewModel>() {
    private val spaceFeeds = 1

    companion object {
        lateinit var adapter: MainUserFeedsAdapter
    }

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
        viewModel.observeCreateFeedsPage().observeNotNull(viewLifecycleOwner, {
            val intent = Intent(requireActivity(), CreateFeedsActivity::class.java)
            intent.putExtra(EXTRA_USER_DATA, it)
            startActivityForResult(intent, REQUEST_CODE_CREATE_FEEDS)
        })

        viewModel.observeEditFeedsPage().observeNotNull(viewLifecycleOwner, {
            val intent = Intent(requireActivity(), EditFeedsActivity::class.java)
            intent.putExtra(EXTRA_EDIT_FEEDS, it)
            startActivityForResult(intent, REQUEST_CODE_EDIT_FEEDS)
        })

        viewModel.observeFindUsersPage().observeNotNull(viewLifecycleOwner, {
            startActivity(Intent(context, SearchUsersActivity::class.java))
        })

        viewModel.observeSeeAllUsersPage().observeNotNull(viewLifecycleOwner, {
            viewModel.getIntentUserData {
                val intent = Intent(context, SeeAllUsersActivity::class.java)
                intent.putExtra(EXTRA_USER_DATA, it)
                startActivityForResult(intent, REQUEST_CODE_SEE_ALL_USERS)
            }
        })

        viewModel.observeReactionsSummaryPage().observeNotNull(viewLifecycleOwner, {
            val intent = Intent(context, ReactionsSummaryFeedsActivity::class.java)
            intent.putExtra(EXTRA_USER_REACTION_DATA, it)
            startActivityForResult(intent, REQUEST_CODE_REACTIONS_SUMMARY)
        })

        viewModel.observeUserPage().observeNotNull(viewLifecycleOwner, {
            val intent = Intent(context, UserFeedsActivity::class.java)
            intent.putExtra(EXTRA_USER_DATA, it)
            startActivityForResult(intent, REQUEST_CODE_USER_FEEDS)
        })
    }

    private fun renderList(viewModel: UserFeedsViewModel) {
        adapter = MainUserFeedsAdapter(context = requireContext(),
                lifecycleOwner = viewLifecycleOwner,
                viewModel = viewModel)

        RecyclerBuilder(context = requireContext(), recyclerView = recycler_main_feeds, spaceCount = spaceFeeds)
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
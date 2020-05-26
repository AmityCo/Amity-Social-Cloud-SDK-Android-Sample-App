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
import com.ekoapp.sample.socialfeature.userfeeds.view.list.EkoUserFeedsMultiViewAdapter
import com.ekoapp.sample.socialfeature.users.view.SeeAllUsersActivity
import kotlinx.android.synthetic.main.fragment_user_feeds.*

class UserFeedsFragment : SingleViewModelFragment<UserFeedsViewModel>() {
    private val spaceFeeds = 1

    companion object {
        lateinit var adapter: EkoUserFeedsMultiViewAdapter
    }

    override fun getLayout(): Int {
        return R.layout.fragment_user_feeds
    }

    override fun bindViewModel(viewModel: UserFeedsViewModel) {
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun setupEvent(viewModel: UserFeedsViewModel) {
        viewModel.observeCreateFeedsPage().observeNotNull(viewLifecycleOwner, {
            val intent = Intent(requireActivity(), CreateFeedsActivity::class.java)
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
            val intent = Intent(context, SeeAllUsersActivity::class.java)
            intent.putExtra(EXTRA_USER_DATA, viewModel.getMyProfile())
            startActivityForResult(intent, REQUEST_CODE_SEE_ALL_USERS)
        })

        viewModel.observeReactionsSummaryPage().observeNotNull(viewLifecycleOwner, {
            val intent = Intent(context, ReactionsSummaryFeedsActivity::class.java)
            intent.putExtra(EXTRA_USER_REACTION_DATA, it)
            startActivityForResult(intent, REQUEST_CODE_REACTIONS_SUMMARY)
        })
    }

    private fun renderList(viewModel: UserFeedsViewModel) {
        adapter = EkoUserFeedsMultiViewAdapter(context = requireContext(),
                lifecycleOwner = viewLifecycleOwner,
                viewModel = viewModel)

        RecyclerBuilder(context = requireContext(), recyclerView = recycler_feeds, spaceCount = spaceFeeds)
                .builder()
                .buildMultiView(adapter)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CREATE_FEEDS -> {
                viewModel?.updateFeeds()
            }
        }
    }
}
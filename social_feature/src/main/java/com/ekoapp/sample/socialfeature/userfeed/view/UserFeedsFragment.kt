package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Intent
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.EXTRA_EDIT_FEEDS
import com.ekoapp.sample.socialfeature.constants.REQUEST_CODE_CREATE_FEEDS
import com.ekoapp.sample.socialfeature.constants.REQUEST_CODE_EDIT_FEEDS
import com.ekoapp.sample.socialfeature.createfeeds.CreateFeedsActivity
import com.ekoapp.sample.socialfeature.di.DaggerSocialFragmentComponent
import com.ekoapp.sample.socialfeature.editfeeds.EditFeedsActivity
import com.ekoapp.sample.socialfeature.userfeed.view.list.EkoUserFeedsMultiViewAdapter
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
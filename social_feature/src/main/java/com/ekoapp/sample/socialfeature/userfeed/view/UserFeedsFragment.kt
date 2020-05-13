package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Intent
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.di.DaggerSocialFragmentComponent
import com.ekoapp.sample.socialfeature.userfeed.EXTRA_DISPLAY_NAME
import com.ekoapp.sample.socialfeature.userfeed.EXTRA_NAME_EDIT_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.REQUEST_CODE_CREATE_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.REQUEST_CODE_EDIT_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.view.createfeeds.CreateFeedsActivity
import com.ekoapp.sample.socialfeature.userfeed.view.editfeeds.EditFeedsActivity
import com.ekoapp.sample.socialfeature.userfeed.view.list.EkoUserFeedsAdapter
import kotlinx.android.synthetic.main.component_touchable_create_feeds.view.*
import kotlinx.android.synthetic.main.fragment_user_feeds.*

class UserFeedsFragment : SingleViewModelFragment<UserFeedsViewModel>() {

    private val spaceFeeds = 1
    private lateinit var adapter: EkoUserFeedsAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_user_feeds
    }

    override fun bindViewModel(viewModel: UserFeedsViewModel) {
        friend_list.renderList(viewLifecycleOwner, viewModel)
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun setupEvent(viewModel: UserFeedsViewModel) {
        touchable_post_feeds.button_touchable_target_post.setOnClickListener {
            val intent = Intent(requireActivity(), CreateFeedsActivity::class.java)
            intent.putExtra(EXTRA_DISPLAY_NAME, EkoClient.getUserId())
            startActivityForResult(intent, REQUEST_CODE_CREATE_FEEDS)
        }

        viewModel.observeEditFeedsPage().observeNotNull(viewLifecycleOwner, {
            val intent = Intent(requireActivity(), EditFeedsActivity::class.java)
            intent.putExtra(EXTRA_NAME_EDIT_FEEDS, it)
            startActivityForResult(intent, REQUEST_CODE_EDIT_FEEDS)
        })
    }

    private fun renderList(viewModel: UserFeedsViewModel) {
        viewModel.getUserFeeds().observeNotNull(viewLifecycleOwner, {
            adapter = EkoUserFeedsAdapter(userFeedsViewModel = viewModel)
            RecyclerBuilder(context = requireContext(), recyclerView = recycler_feeds, spaceCount = spaceFeeds)
                    .builder()
                    .build(adapter)
            adapter.submitList(it)
        })
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
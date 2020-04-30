package com.ekoapp.sample.socialfeature.userfeed.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.EXTRA_NAME_CREATE_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.REQUEST_CODE_CREATE_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.di.DaggerSocialFragmentComponent
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.list.UserFeedsAdapter
import kotlinx.android.synthetic.main.component_touchable_create_feeds.view.*
import kotlinx.android.synthetic.main.fragment_user_feeds.*
import timber.log.Timber

class UserFeedsFragment : SingleViewModelFragment<UserFeedsViewModel>() {

    private val spaceFeeds = 1
    private lateinit var adapter: UserFeedsAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_user_feeds
    }

    override fun bindViewModel(viewModel: UserFeedsViewModel) {
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun setupEvent(viewModel: UserFeedsViewModel) {
        touchable_post_feeds.button_touchable_target_post.setOnClickListener {
            Timber.d(getCurrentClassAndMethodNames())
            startActivityForResult(Intent(requireActivity(), CreateFeedsActivity::class.java), REQUEST_CODE_CREATE_FEEDS)
        }

        viewModel.renderDelete().observeNotNull(viewLifecycleOwner, {
            viewModel.updateDeletedFeeds(it, adapter::deleteItem)
        })
    }

    private fun renderList(viewModel: UserFeedsViewModel) {
        adapter = UserFeedsAdapter(requireContext(), viewModel.getUserFeeds().toMutableList(), userFeedsViewModel = viewModel)
        RecyclerBuilder(context = requireContext(), recyclerView = recycler_feeds, spaceCount = spaceFeeds)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREATE_FEEDS && resultCode == RESULT_OK) {
            addFeeds(data)
        }
    }

    private fun addFeeds(data: Intent?) {
        data?.let {
            val item = data.getParcelableExtra<SampleFeedsResponse>(EXTRA_NAME_CREATE_FEEDS)
            adapter.addItem(data = item)
        }
    }
}
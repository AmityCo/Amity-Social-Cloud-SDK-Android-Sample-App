package com.ekoapp.sample.chatfeature.channels

import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channels.list.ChannelsAdapter
import com.ekoapp.sample.chatfeature.di.DaggerChatFragmentComponent
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import kotlinx.android.synthetic.main.fragment_channels.*

class ChannelsFragment : SingleViewModelFragment<ChannelsViewModel>() {
    private lateinit var adapter: ChannelsAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_channels
    }

    override fun bindViewModel(viewModel: ChannelsViewModel) {
        setupView(viewModel)
        renderList(viewModel)
    }

    private fun setupView(viewModel: ChannelsViewModel) {
        viewModel.bindTotalUnreadCount().observeNotNull(viewLifecycleOwner, avatar_with_total::setupView)
    }

    private fun renderList(viewModel: ChannelsViewModel) {
        adapter = ChannelsAdapter(requireContext())
        RecyclerBuilder(context = requireContext(), recyclerView = recycler_channels)
                .builder()
                .build(adapter)
        viewModel.bindChannelCollection().observeNotNull(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    override fun getViewModelClass(): Class<ChannelsViewModel> {
        return ChannelsViewModel::class.java
    }

    override fun initDependencyInjection() {
        DaggerChatFragmentComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}
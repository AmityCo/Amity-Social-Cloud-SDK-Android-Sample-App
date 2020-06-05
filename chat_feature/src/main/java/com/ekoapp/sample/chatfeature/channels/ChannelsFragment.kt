package com.ekoapp.sample.chatfeature.channels

import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.channels.list.MainChannelsAdapter
import com.ekoapp.sample.chatfeature.di.DaggerChatFragmentComponent
import com.ekoapp.sample.core.base.list.IMMEDIATELY_SCROLL
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import kotlinx.android.synthetic.main.fragment_channels.*

class ChannelsFragment : SingleViewModelFragment<ChannelsViewModel>() {
    private lateinit var adapter: MainChannelsAdapter
    private lateinit var recyclerBuilder: RecyclerBuilder

    override fun getLayout(): Int {
        return R.layout.fragment_channels
    }

    override fun bindViewModel(viewModel: ChannelsViewModel) {
        setupView(viewModel)
        renderList(viewModel)
    }

    private fun setupView(viewModel: ChannelsViewModel) {
        viewModel.bindTotalUnreadCount().observeNotNull(viewLifecycleOwner, header_channels::setTotal)
        header_channels.setupEvent(
                actionConversation = {
                    header_channels.renderUsers(childFragmentManager, viewLifecycleOwner, viewModel)
                },
                actionCreateChannel = {
                    header_channels.renderCreateChannel(childFragmentManager)
                },
                actionSettings = {
                    header_channels.renderSettings(childFragmentManager,
                            general = {

                            },
                            channel = {

                            })
                })
        header_channels.createStandardChannel(childFragmentManager) {
            viewModel.bindCreateChannel(it)
            recyclerBuilder.smoothScrollToPosition(delay = IMMEDIATELY_SCROLL)
        }
        header_channels.createPrivateChannel(childFragmentManager) {
            viewModel.bindCreateChannel(it)
            recyclerBuilder.smoothScrollToPosition(delay = IMMEDIATELY_SCROLL)
        }

    }

    private fun renderList(viewModel: ChannelsViewModel) {
        adapter = MainChannelsAdapter(requireContext(), viewModel)
        recyclerBuilder = RecyclerBuilder(context = requireContext(), recyclerView = recycler_main_channels)
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
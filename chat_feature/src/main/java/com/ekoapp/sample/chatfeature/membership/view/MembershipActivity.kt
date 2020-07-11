package com.ekoapp.sample.chatfeature.membership.view

import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.constants.EXTRA_CHANNEL_MESSAGES
import com.ekoapp.sample.chatfeature.data.ChannelData
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.chatfeature.membership.view.list.EkoChannelMembershipAdapter
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import kotlinx.android.synthetic.main.activity_channel_membership.*

class MembershipActivity : SingleViewModelActivity<MembershipViewModel>() {
    private lateinit var adapter: EkoChannelMembershipAdapter

    override fun getLayout(): Int {
        return R.layout.activity_channel_membership
    }

    override fun bindViewModel(viewModel: MembershipViewModel) {
        val channelMessage = intent.extras?.getParcelable<ChannelData>(EXTRA_CHANNEL_MESSAGES)
        viewModel.setupIntent(channelMessage)
        setupAppBar(viewModel)
        renderList(viewModel)
    }

    private fun setupAppBar(viewModel: MembershipViewModel) {
        appbar_membership.setup(this, true)
        viewModel.getIntentChannelData {
            appbar_membership.setTitle(it.channelId)
        }
    }

    private fun renderList(viewModel: MembershipViewModel) {
        adapter = EkoChannelMembershipAdapter(this, viewModel)
        RecyclerBuilder(context = this, recyclerView = recycler_users)
                .builder()
                .build(adapter)
        viewModel.getIntentChannelData { data ->
            viewModel.bindGetMembership(data.channelId).observeNotNull(this, {
                header_users.setupView(it)
                adapter.submitList(it)
            })
        }
    }

    override fun getViewModelClass(): Class<MembershipViewModel> {
        return MembershipViewModel::class.java
    }

    override fun initDependencyInjection() {
        DaggerChatActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}

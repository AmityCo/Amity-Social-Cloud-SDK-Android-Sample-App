package com.ekoapp.sample.chatfeature

import com.ekoapp.sample.chatfeature.channels.ChannelsFragment
import com.ekoapp.sample.chatfeature.di.DaggerChatFragmentComponent
import com.ekoapp.sample.core.base.replaceFragment
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelFragment
import com.ekoapp.sample.core.ui.extensions.coreComponent


class ChatFragment : SingleViewModelFragment<ChatViewModel>() {

    override fun getLayout(): Int {
        return R.layout.fragment_chat
    }

    override fun bindViewModel(viewModel: ChatViewModel) {
        replaceFragment(R.id.main_content_frame, ChannelsFragment())
    }

    override fun getViewModelClass(): Class<ChatViewModel> {
        return ChatViewModel::class.java
    }

    override fun initDependencyInjection() {
        DaggerChatFragmentComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

}
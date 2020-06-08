package com.ekoapp.sample.chatfeature.messages

import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent

class MessagesActivity : SingleViewModelActivity<MessagesViewModel>() {

    override fun bindViewModel(viewModel: MessagesViewModel) {

    }

    override fun getViewModelClass(): Class<MessagesViewModel> {
        return MessagesViewModel::class.java
    }

    override fun getLayout(): Int {
        return R.layout.activity_messages
    }

    override fun initDependencyInjection() {
        DaggerChatActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}
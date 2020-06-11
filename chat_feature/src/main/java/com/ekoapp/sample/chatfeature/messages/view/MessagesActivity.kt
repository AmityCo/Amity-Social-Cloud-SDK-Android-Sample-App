package com.ekoapp.sample.chatfeature.messages.view

import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.constants.EXTRA_CHANNEL_MESSAGES
import com.ekoapp.sample.chatfeature.data.ChannelData
import com.ekoapp.sample.chatfeature.data.MessageData
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.chatfeature.messages.view.list.MainMessageAdapter
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import kotlinx.android.synthetic.main.activity_messages.*

class MessagesActivity : SingleViewModelActivity<MessagesViewModel>() {

    private lateinit var adapter: MainMessageAdapter

    override fun bindViewModel(viewModel: MessagesViewModel) {
        val item = intent.extras?.getParcelable<ChannelData>(EXTRA_CHANNEL_MESSAGES)
        viewModel.setupIntent(item)
        setupAppBar()
        renderList(viewModel)
    }

    private fun renderList(viewModel: MessagesViewModel) {
        adapter = MainMessageAdapter(this, viewModel)
        val recyclerBuilder = RecyclerBuilder(context = this, recyclerView = recycler_message)
                .builder()
                .build(adapter)
        viewModel.getIntentChannelData {
            viewModel.bindGetMessageCollectionByTags(MessageData(channelId = it.channelId))
                    .observeNotNull(this, { items ->
                        adapter.submitList(items)
                        recyclerBuilder.observeScrollToBottom(viewModel, items)
                    })

            viewModel.message(it.channelId, send_message.text())
            viewModel.observeMessage().observeNotNull(this, viewModel::bindSendTextMessage)
        }

    }

    private fun RecyclerBuilder.observeScrollToBottom(viewModel: MessagesViewModel, items: PagedList<EkoMessage>) {
        viewModel.observeScrollToBottom().observeNotNull(this@MessagesActivity, {
            smoothScrollToPosition(position = items.size - 1)
        })
    }

    private fun setupAppBar() {
        appbar_message.setup(this, true)
        appbar_message.setTitle(getString(R.string.temporarily_chat))
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
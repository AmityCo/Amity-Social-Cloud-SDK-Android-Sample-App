package com.ekoapp.sample.chatfeature.messages.view

import android.app.Activity
import android.content.Intent
import android.view.Menu
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.constants.EXTRA_CHANNEL_MESSAGES
import com.ekoapp.sample.chatfeature.constants.EXTRA_REPLY_MESSAGES
import com.ekoapp.sample.chatfeature.data.ChannelData
import com.ekoapp.sample.chatfeature.data.MessageData
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.chatfeature.intents.openReplyMessagesPage
import com.ekoapp.sample.chatfeature.messages.view.list.MainMessageAdapter
import com.ekoapp.sample.chatfeature.toolbars.MessageToolbarMenu
import com.ekoapp.sample.core.base.components.toolbar.ToolbarMenu
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.constants.PICKFILE_REQUEST_CODE
import com.ekoapp.sample.core.intent.IntentRequestCode
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.core.ui.extensions.observeOnce
import kotlinx.android.synthetic.main.activity_messages.*


class MessagesActivity : SingleViewModelActivity<MessagesViewModel>() {

    private lateinit var adapter: MainMessageAdapter

    override fun getToolbar(): ToolbarMenu? {
        return MessageToolbarMenu(
                eventMember = {

                },
                eventNotification = {

                },
                eventLeaveChannel = {
                    viewModel?.getIntentChannelData {
                        viewModel?.bindLeaveChannel(it.channelId)
                        onBackPressed()
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_message, menu)
        return true
    }

    override fun bindViewModel(viewModel: MessagesViewModel) {
        val channelMessage = intent.extras?.getParcelable<ChannelData>(EXTRA_CHANNEL_MESSAGES)
        val replyMessage = intent.extras?.getParcelable<MessageData>(EXTRA_REPLY_MESSAGES)
        viewModel.setupIntent(channelMessage)
        viewModel.setupIntent(replyMessage)
        setupAppBar(viewModel)
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun renderList(viewModel: MessagesViewModel) {
        adapter = MainMessageAdapter(this, viewModel)
        val recyclerBuilder = RecyclerBuilder(context = this, recyclerView = recycler_message)
                .stackFromEnd(true)
                .build(adapter)
        viewModel.getIntentChannelData {
            viewModel.bindGetMessageCollectionByTags(MessageData(channelId = it.channelId))
                    .observeNotNull(this, { items ->
                        adapter.submitList(items)
                        recyclerBuilder.afterSent(viewModel, items)
                    })
            it.renderSendMessage(viewModel)
        }
        viewModel.getIntentMessageData {
            viewModel.bindGetMessageCollectionByTags(it)
                    .observeNotNull(this, { items ->
                        adapter.submitList(items)
                        recyclerBuilder.afterSent(viewModel, items)
                    })
            ChannelData(channelId = it.channelId, parentId = it.parentId).renderSendMessage(viewModel)
        }
    }

    private fun ChannelData.renderSendMessage(viewModel: MessagesViewModel) {
        main_send_message.renderTextSending(channelId = channelId)
        main_send_message.renderSelectPhoto(fm = supportFragmentManager)
        main_send_message.renderSelectFile()

        viewModel.observeReplying().observeNotNull(this@MessagesActivity, main_send_message::renderReplying)
        viewModel.initMessage(main_send_message.message())
        viewModel.observeMessage().observeNotNull(this@MessagesActivity, viewModel::bindSendMessage)
    }

    private fun RecyclerBuilder.afterSent(viewModel: MessagesViewModel, items: PagedList<EkoMessage>) {
        viewModel.observeAfterSent().observeOnce(this@MessagesActivity, {
            smoothScrollToPosition(position = viewModel.getScrollPosition(items.size))
        })
    }

    private fun setupEvent(viewModel: MessagesViewModel) {
        viewModel.observeViewReply().observeNotNull(this, this::openReplyMessagesPage)
    }

    private fun setupAppBar(viewModel: MessagesViewModel) {
        appbar_message.setup(this, true)
        viewModel.getIntentChannelData {
            appbar_message.setTitle(it.channelId)
        }
        viewModel.getIntentMessageData {
            appbar_message.setTitle(String.format(getString(R.string.temporarily_replied_to, it.parentId)))
        }
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

    override fun onStart() {
        super.onStart()
        viewModel?.apply {
            getIntentChannelData { bindStartReading(it.channelId) }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel?.apply {
            getIntentChannelData { bindStopReading(it.channelId) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IntentRequestCode.REQUEST_TAKE_PHOTO) {
            viewModel?.getIntentChannelData {
                main_send_message.renderImageSending(it.channelId)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == IntentRequestCode.REQUEST_SELECT_PHOTO) {
            viewModel?.getIntentChannelData {
                main_send_message.renderImageSending(it.channelId, data?.data)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == PICKFILE_REQUEST_CODE) {
            viewModel?.getIntentChannelData {
                main_send_message.renderAttachSending(it.channelId, data?.data)
            }
        }
    }
}
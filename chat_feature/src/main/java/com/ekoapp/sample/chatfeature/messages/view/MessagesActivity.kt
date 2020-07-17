package com.ekoapp.sample.chatfeature.messages.view

import android.app.Activity
import android.content.Intent
import android.view.Menu
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.constants.EXTRA_CHANNEL_MESSAGES
import com.ekoapp.sample.chatfeature.data.ChannelData
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.chatfeature.intents.openMembershipPage
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
import com.ekoapp.sample.core.utils.SnackBarUtil
import kotlinx.android.synthetic.main.activity_messages.*


class MessagesActivity : SingleViewModelActivity<MessagesViewModel>() {

    private lateinit var adapter: MainMessageAdapter

    override fun getToolbar(): ToolbarMenu? {
        viewModel?.setTitleNotification()
        return MessageToolbarMenu(
                notificationMenu = { menu ->
                    viewModel?.apply {
                        observeNotificationTitle().observeNotNull(this@MessagesActivity, {
                            menu.title = it
                        })
                    }
                },
                eventMember = {
                    viewModel?.getIntentChannelData(this::openMembershipPage)
                },
                eventNotification = {
                    viewModel?.settingNotification()
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
        viewModel.setupIntent(channelMessage)
        viewModel.observeNotification().observeNotNull(this, viewModel::bindSetNotification)
        setupAppBar(viewModel)
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun renderList(viewModel: MessagesViewModel) {
        adapter = MainMessageAdapter(this, this, viewModel)
        val recyclerBuilder = RecyclerBuilder(context = this, recyclerView = recycler_message)
                .stackFromEnd(true)
                .build(adapter)
        viewModel.getIntentChannelData {
            viewModel.bindGetMessageCollectionByTags(it)
                    .observeNotNull(this, { items ->
                        adapter.submitList(items)
                        recyclerBuilder.afterSent(viewModel, items)
                    })
            it.renderSendMessage(viewModel)
        }
    }

    private fun ChannelData.renderSendMessage(viewModel: MessagesViewModel) {
        main_send_message.renderTextSending()
        main_send_message.renderSelectPhoto(fm = supportFragmentManager)
        main_send_message.renderSelectFile()
        main_send_message.renderCustomSending(fm = supportFragmentManager)
        viewModel.observeClickReply().observeNotNull(this@MessagesActivity, {
            main_send_message.renderReplyingView(it) { messageId -> viewModel.replyMessageId = messageId }
        })

        viewModel.initMessage(this, main_send_message.message())
        viewModel.observeMessage().observeNotNull(this@MessagesActivity, viewModel::bindSendMessage)
    }

    private fun RecyclerBuilder.afterSent(viewModel: MessagesViewModel, items: PagedList<EkoMessage>) {
        viewModel.observeAfterSent().observeOnce(this@MessagesActivity, {
            smoothScrollToPosition(position = viewModel.getScrollPosition(items.size))
        })
    }

    private fun setupEvent(viewModel: MessagesViewModel) {
        viewModel.observeClickViewReply().observeNotNull(this, this::openReplyMessagesPage)
        viewModel.observeReportMessage().observeNotNull(this, SnackBarUtil(fragmentActivity = this)::info)
        viewModel.observeReportSender().observeNotNull(this, SnackBarUtil(fragmentActivity = this)::info)
    }

    private fun setupAppBar(viewModel: MessagesViewModel) {
        appbar_message.setup(this, true)
        viewModel.getIntentChannelData {
            if (it.parentId == null) {
                appbar_message.setTitle(it.channelId)
            } else {
                appbar_message.setTitle(String.format(getString(R.string.temporarily_replied_to, it.parentId)))
            }
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
            main_send_message.renderImageSending()
        }

        if (resultCode == Activity.RESULT_OK && requestCode == IntentRequestCode.REQUEST_SELECT_PHOTO) {
            main_send_message.renderImageSending(uri = data?.data)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == PICKFILE_REQUEST_CODE) {
            main_send_message.renderAttachSending(uri = data?.data)
        }
    }
}
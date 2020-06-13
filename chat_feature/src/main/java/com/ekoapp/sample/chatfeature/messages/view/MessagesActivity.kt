package com.ekoapp.sample.chatfeature.messages.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import com.ekoapp.sample.core.constants.REQUEST_CODE_CAMERA
import com.ekoapp.sample.core.constants.REQUEST_CODE_GALLERY
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.core.ui.extensions.observeOnce
import com.ekoapp.sample.core.utils.getImageUri
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
    }

    private fun ChannelData.renderSendMessage(viewModel: MessagesViewModel) {
        main_send_message.renderTextSending(channelId = channelId)
        main_send_message.renderSelectPhoto(fm = supportFragmentManager)

        viewModel.observeReplying().observeNotNull(this@MessagesActivity, main_send_message::renderReplying)
        viewModel.initMessage(main_send_message.message())
        viewModel.observeMessage().observeNotNull(this@MessagesActivity, viewModel::bindSendMessage)
    }

    private fun RecyclerBuilder.afterSent(viewModel: MessagesViewModel, items: PagedList<EkoMessage>) {
        viewModel.observeAfterSent().observeOnce(this@MessagesActivity, {
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
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            viewModel?.getIntentChannelData {
                val bitmap = data?.extras?.get("data") as Bitmap
                main_send_message.renderImageSending(it.channelId,
                        getImageUri(bitmap, Bitmap.CompressFormat.JPEG, 100))
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY) {
            viewModel?.getIntentChannelData {
                val uri = data?.data
                uri?.apply { main_send_message.renderImageSending(it.channelId, this) }
            }
        }
    }
}
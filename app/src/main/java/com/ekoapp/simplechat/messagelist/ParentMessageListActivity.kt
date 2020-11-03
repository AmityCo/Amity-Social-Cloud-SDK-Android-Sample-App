package com.ekoapp.simplechat.messagelist

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.ekosdk.message.EkoMessage
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.intent.*
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_message_list.*

class ParentMessageListActivity : MessageListActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setUpListeners()
    }

    override fun getChannelId(): String {
        val channel = ViewParentMessagesIntent.getChannel(intent)
        return channel.getChannelId()
    }

    override fun setTitleName() {}
    override fun setSubtitleName() {
        val channel = ViewParentMessagesIntent.getChannel(intent)
        toolbar.subtitle = String.format("unreadCount: %s messageCount:%s", channel.getUnreadCount(), channel.getMessageCount())
    }

    override fun getMenu(): Int {
        return R.menu.menu_parent_message_list
    }

    override fun getMessageCollection(): LiveData<PagedList<EkoMessage>> {
        return LiveDataReactiveStreams.fromPublisher(
                messageRepository.getMessageCollection(getChannelId())
                        .parentId(null)
                        .includingTags(EkoTags(includingTags))
                        .excludingTags(EkoTags(excludingTags))
                        .stackFromEnd(stackFromEnd.get())
                        .build()
                        .query()
        )
    }

    override fun getDefaultStackFromEnd(): Boolean {
        return true
    }

    override fun getDefaultRevertLayout(): Boolean {
        return false
    }

    override fun startReading() {
        channelRepository.membership(getChannelId()).startReading()
    }

    override fun stopReading() {
        channelRepository.membership(getChannelId()).stopReading()
    }

    override fun onClick(message: EkoMessage) {
        if (message.isDeleted()) {
            return
        }
        val intent = when (val data = message.getData()) {
            is EkoMessage.Data.TEXT -> {
                ViewChildMessagesIntent(this, message, data)
            }
            is EkoMessage.Data.IMAGE -> {
                ViewChildMessagesIntent(this, message, data)
            }
            is EkoMessage.Data.FILE -> {
                ViewChildMessagesIntent(this, message, data)
            }
            is EkoMessage.Data.CUSTOM -> {
                ViewChildMessagesIntent(this, message, data)
            }
        }
        startActivity(intent)
    }

    override fun createTextMessage(text: String): Completable {
        return messageRepository.createMessage(getChannelId())
                .with()
                .text(text)
                .build()
                .send()
    }

    private fun setUpListeners() {
        message_image_button.setOnClickListener {
            startActivityForResult(OpenImageMessageSenderIntent(this, getChannelId()),
                    IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE)
        }

        message_file_button.setOnClickListener {
            startActivityForResult(OpenFileMessageSenderIntent(this, getChannelId()),
                    IntentRequestCode.REQUEST_SEND_FILE_MESSAGE)
        }

        message_custom_button.setOnClickListener {
            startActivityForResult(OpenCustomMessageSenderIntent(this, getChannelId()),
                    IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE)
        }
    }
}
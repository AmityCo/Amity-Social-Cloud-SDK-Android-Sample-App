package com.ekoapp.simplechat.messagelist

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.paging.PagedList
import com.ekoapp.ekosdk.message.EkoMessage
import io.reactivex.Completable
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.intent.*
import kotlinx.android.synthetic.main.activity_message_list.*

class ChildMessageListActivity : MessageListActivity() {
    override fun onCreate(savedState: Bundle?) {
        parentMessage = ViewChildMessagesIntent.getMessage(intent)
        messageData = ViewChildMessagesIntent.getMessageData(intent)
        super.onCreate(savedState)
        setUpListeners()
    }

    lateinit var parentMessage: EkoMessage

    lateinit var messageData: EkoMessage.Data

    override fun getChannelId(): String {
        return parentMessage.getChannelId()
    }

    override fun setTitleName() {}

    override fun setSubtitleName() {
        when (messageData) {
            is EkoMessage.Data.TEXT -> {
                toolbar.subtitle = ViewChildMessagesIntent.getTextMessageData(intent).getText()
            }

            is EkoMessage.Data.IMAGE -> {
                toolbar.subtitle = ViewChildMessagesIntent.getImageMessageData(intent).getUrl()
            }

            is EkoMessage.Data.FILE -> {
                toolbar.subtitle = ViewChildMessagesIntent.getFileMessageData(intent).getUrl()
            }

            is EkoMessage.Data.CUSTOM -> {
                toolbar.subtitle = ViewChildMessagesIntent.getCustomMessageData(intent).raw().toString()
            }
            else -> {
                toolbar.subtitle = parentMessage.getMessageId()
            }
        }
    }

    override fun getMenu(): Int {
        return R.menu.menu_child_message_list
    }

    override fun getMessageCollection(): LiveData<PagedList<EkoMessage>> {
        return LiveDataReactiveStreams.fromPublisher(
                messageRepository.getMessageCollection(getChannelId())
                        .parentId(parentMessage.getMessageId())
                        .includingTags(EkoTags(includingTags))
                        .excludingTags(EkoTags(excludingTags))
                        .stackFromEnd(stackFromEnd.get())
                        .build()
                        .query()
        )
    }

    override fun getDefaultStackFromEnd(): Boolean {
        return false
    }

    override fun getDefaultRevertLayout(): Boolean {
        return false
    }

    override fun startReading() {}
    override fun stopReading() {}
    override fun onClick(message: EkoMessage) { }
    override fun createTextMessage(text: String): Completable {
        return messageRepository.createMessage(getChannelId())
                .parentId(parentMessage.getMessageId())
                .with()
                .text(text)
                .build()
                .send()
    }

    private fun setUpListeners() {
        message_image_button.setOnClickListener {
            startActivityForResult(OpenImageMessageSenderIntent(this,
                    getChannelId(), parentMessage.getMessageId()), IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE)
        }

        message_file_button.setOnClickListener {
            startActivityForResult(OpenFileMessageSenderIntent(this,
                    getChannelId(), parentMessage.getMessageId()), IntentRequestCode.REQUEST_SEND_FILE_MESSAGE)
        }

        message_custom_button.setOnClickListener {
            startActivityForResult(OpenCustomMessageSenderIntent(this,
                    getChannelId(), parentMessage.getMessageId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE)
        }
    }
}
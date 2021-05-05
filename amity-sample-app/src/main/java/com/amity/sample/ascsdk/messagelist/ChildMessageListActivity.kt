package com.amity.sample.ascsdk.messagelist

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.paging.PagedList
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.*
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.sdk.core.AmityTags
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_message_list.*


class ChildMessageListActivity : MessageListActivity() {

    override fun onCreate(savedState: Bundle?) {
        parentMessage = ViewChildMessagesIntent.getMessage(intent)
        messageData = ViewChildMessagesIntent.getMessageData(intent)
        super.onCreate(savedState)
        setUpListeners()
    }

    lateinit var parentMessage: AmityMessage

    lateinit var messageData: AmityMessage.Data

    override fun getChannelId(): String {
        return parentMessage.getChannelId()
    }

    override fun setTitleName() {}

    override fun setSubtitleName() {
        val data = messageData
        // test parcelable data
        when (data) {
            is AmityMessage.Data.TEXT -> {
                toolbar.subtitle = ViewChildMessagesIntent.getTextMessageData(intent).getText()
            }

            is AmityMessage.Data.IMAGE -> {
                toolbar.subtitle = ViewChildMessagesIntent.getImageMessageData(intent).getImage()?.getUrl()
            }

            is AmityMessage.Data.FILE -> {
                toolbar.subtitle = ViewChildMessagesIntent.getFileMessageData(intent).getFile()?.getUrl()
            }

            is AmityMessage.Data.CUSTOM -> {
                toolbar.subtitle = ViewChildMessagesIntent.getCustomMessageData(intent).getRawData().toString()
            }
            else -> {
                toolbar.subtitle = parentMessage.getMessageId()
            }
        }
    }

    override fun getMenu(): Int {
        return R.menu.menu_child_message_list
    }

    override fun getMessageCollection(): LiveData<PagedList<AmityMessage>> {
        return LiveDataReactiveStreams.fromPublisher(
                messageRepository.getMessages(getChannelId())
                        .parentId(parentMessage.getMessageId())
                        .includingTags(AmityTags(includingTags))
                        .excludingTags(AmityTags(excludingTags))
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
    override fun onClick(message: AmityMessage) {}
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
            startActivityForResult(OpenImageMessageSenderIntent(this, getChannelId(), parentMessage.getMessageId()), IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE)
        }
        message_file_button.setOnClickListener {
            startActivityForResult(OpenFileMessageSenderIntent(this, getChannelId(), parentMessage.getMessageId()), IntentRequestCode.REQUEST_SEND_FILE_MESSAGE)
        }

        message_custom_button.setOnClickListener {
            startActivityForResult(OpenCustomMessageSenderIntent(this, getChannelId(), parentMessage.getMessageId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE)
        }

        message_audio_button.setOnClickListener {
            startActivityForResult(OpenAudioMessageSenderIntent(this, getChannelId(), parentMessage.getMessageId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE)
        }
    }
}

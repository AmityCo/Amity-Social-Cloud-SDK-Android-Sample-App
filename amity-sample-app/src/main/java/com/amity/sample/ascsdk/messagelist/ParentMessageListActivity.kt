package com.amity.sample.ascsdk.messagelist

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.paging.PagedList
import com.amity.sample.ascsdk.intent.*
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.sdk.core.AmityTags
import com.amity.sample.ascsdk.R
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

    override fun getMessageCollection(): LiveData<PagedList<AmityMessage>> {
        return LiveDataReactiveStreams.fromPublisher(
                messageRepository.getMessages(getChannelId())
                        .parentId(null)
                        .includingTags(AmityTags(includingTags))
                        .excludingTags(AmityTags(excludingTags))
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

    override fun onClick(message: AmityMessage) {
        if (message.isDeleted()) {
            return
        }

        // test parcelable data
        val data = message.getData()
        var intent = ViewChildMessagesIntent(this, message, data)
        when (data) {
            is AmityMessage.Data.TEXT -> {
                intent = ViewChildMessagesIntent(this, message, data)
            }
            is AmityMessage.Data.IMAGE -> {
                intent = ViewChildMessagesIntent(this, message, data)
            }
            is AmityMessage.Data.FILE -> {
                intent = ViewChildMessagesIntent(this, message, data)
            }
            is AmityMessage.Data.CUSTOM -> {
                intent = ViewChildMessagesIntent(this, message, data)
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
            startActivityForResult(OpenImageMessageSenderIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE)
        }

        message_file_button.setOnClickListener {
            startActivityForResult(OpenFileMessageSenderIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_FILE_MESSAGE)
        }

        message_custom_button.setOnClickListener {
            startActivityForResult(OpenCustomMessageSenderIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE)
        }

        message_audio_button.setOnClickListener {
            startActivityForResult(OpenAudioMessageSenderIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE)
        }
    }

}

package com.ekoapp.sample.chatfeature.messagelist

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.intent.*
import com.ekoapp.sample.core.intent.IntentRequestCode
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_message_list.*

class ParentMessageListActivity : MessageListActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setUpMessageToolbar()
    }

    override fun getChannelId(): String {
        return ViewParentMessagesIntent.getChannelId(intent)
    }

    override fun setTitleName() {
        toolbar.title = getChannelId()
    }
    override fun setSubtitleName() {
        channelRepository.getChannel(getChannelId())
                .observe(this, Observer { channel: EkoChannel ->
                    toolbar.setSubtitle(String.format("unreadCount: %s messageCount:%s",
                            channel.unreadCount,
                            channel.messageCount))
                })
    }

    override fun getMenu(): Int {
        return R.menu.menu_parent_message_list
    }

    override fun getMessageCollection(): LiveData<PagedList<EkoMessage>> {
        return messageRepository.getMessageCollectionByTags(getChannelId(),
                null,
                EkoTags(includingTags),
                EkoTags(excludingTags), stackFromEnd.get())
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
        if (message.isDeleted) {
            return
        }
        val intent = ViewChildMessagesIntent(this,
                message.channelId,
                message.messageId,
                if (message.data.has("text")) message.data["text"].asString else "image")
        startActivity(intent)
    }

    override fun createTextMessage(text: String): Completable {
        return messageRepository.createMessage(getChannelId())
                .text(text)
                .build()
                .send()
    }

    private fun setUpMessageToolbar() {
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

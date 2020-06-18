package com.ekoapp.sample.chatfeature.messagelist

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.intent.OpenCustomMessageSenderIntent
import com.ekoapp.sample.chatfeature.intent.OpenFileMessageSenderIntent
import com.ekoapp.sample.chatfeature.intent.OpenImageMessageSenderIntent
import com.ekoapp.sample.chatfeature.intent.ViewChildMessagesIntent
import com.ekoapp.sample.core.intent.IntentRequestCode
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_message_list.*

class ChildMessageListActivity : MessageListActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setUpMessageToolbar()
    }

    override fun getChannelId(): String {
        return ViewChildMessagesIntent.getChannelId(intent)
    }

    val parentId: String
        get() = ViewChildMessagesIntent.getParentId(intent)

    val data: String
        get() = ViewChildMessagesIntent.getData(intent)

    override fun setTitleName() {
        appbar_message.setTitle(getString(R.string.toolbar_child_message_list))
    }
    override fun setSubtitleName() {
       /* toolbar.subtitle = data*/
    }

    override fun getMenu(): Int {
        return R.menu.menu_child_message_list
    }

    override fun getMessageCollection(): LiveData<PagedList<EkoMessage>> {
        return messageRepository.getMessageCollectionByTags(getChannelId(),
                parentId,
                EkoTags(includingTags),
                EkoTags(excludingTags), stackFromEnd.get())
    }

    override fun getDefaultStackFromEnd(): Boolean {
        return false
    }

    override fun getDefaultRevertLayout(): Boolean {
        return false
    }

    override fun startReading() {}
    override fun stopReading() {}
    override fun onClick(message: EkoMessage) {}
    override fun createTextMessage(text: String): Completable {
        return messageRepository.createMessage(getChannelId())
                .text(text)
                .parentId(parentId)
                .build()
                .send()
    }

    private fun setUpMessageToolbar() {
        message_image_button.setOnClickListener {
            startActivityForResult(OpenImageMessageSenderIntent(this, getChannelId(), parentId),
                    IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE)
        }
        message_file_button.setOnClickListener {
            startActivityForResult(OpenFileMessageSenderIntent(this, getChannelId(), parentId),
                    IntentRequestCode.REQUEST_SEND_FILE_MESSAGE)
        }
        message_custom_button.setOnClickListener {
            startActivityForResult(OpenCustomMessageSenderIntent(this, getChannelId(), parentId),
                    IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE)
        }
    }

}

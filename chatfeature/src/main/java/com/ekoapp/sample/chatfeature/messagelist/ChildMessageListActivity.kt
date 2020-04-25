package com.ekoapp.sample.chatfeature.messagelist

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import butterknife.OnClick
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.intent.*
import com.ekoapp.sample.utils.PreferenceHelper.defaultPreference
import com.ekoapp.sample.utils.PreferenceHelper.stackFromEnd
import io.reactivex.Completable


class ChildMessageListActivity : MessageListActivity() {
    override fun getChannelId(): String {
        return ViewChildMessagesIntent.getChannelId(intent)
    }

    val parentId: String
        get() = ViewChildMessagesIntent.getParentId(intent)

    val data: String
        get() = ViewChildMessagesIntent.getData(intent)

    override fun setTitleName() {}
    override fun setSubtitleName() {
        toolbar!!.subtitle = data
    }

    override fun getMenu(): Int {
        return R.menu.menu_child_message_list
    }

    override fun getMessageCollection(): LiveData<PagedList<EkoMessage>> {
        return messageRepository.getMessageCollectionByTags(getChannelId(),
                parentId,
                EkoTags(includingTags),
                EkoTags(excludingTags), defaultPreference(this).stackFromEnd)
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

    @OnClick(R.id.message_image_button)
    fun sendImageMessage() {
        startActivityForResult(OpenImageMessageSenderIntent(this, getChannelId(), parentId), IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE)
    }

    @OnClick(R.id.message_file_button)
    fun sendFileMessage() {
        startActivityForResult(OpenFileMessageSenderIntent(this, getChannelId(), parentId), IntentRequestCode.REQUEST_SEND_FILE_MESSAGE)
    }

    @OnClick(R.id.message_custom_button)
    fun sendCustomMessage() {
        startActivityForResult(OpenCustomMessageSenderIntent(this, getChannelId(), parentId), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE)
    }
}
package com.ekoapp.sample.chatfeature.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import io.reactivex.Completable
import javax.inject.Inject

class MessageRepository @Inject constructor() {

    fun unFlag(messageId: String): Completable = EkoClient.newMessageRepository().report(messageId).unflag()

    fun flag(messageId: String): Completable = EkoClient.newMessageRepository().report(messageId).flag()

    fun setTags(messageId: String, tags: EkoTags): Completable {
        return EkoClient.newMessageRepository().setTags(messageId, tags)
    }

    fun getMessageCollectionByTags(channelId: String,
                                   includingTags: EkoTags,
                                   excludingTags: EkoTags,
                                   stackFromEnd: Boolean): LiveData<PagedList<EkoMessage>> {
        return EkoClient.newMessageRepository().getMessageCollectionByTags(channelId,
                null,
                EkoTags(includingTags),
                EkoTags(excludingTags),
                stackFromEnd)
    }

    fun createMessage(channelId: String, text: String): Completable {
        return EkoClient.newMessageRepository().createMessage(channelId)
                .text(text)
                .build()
                .send()
    }

}
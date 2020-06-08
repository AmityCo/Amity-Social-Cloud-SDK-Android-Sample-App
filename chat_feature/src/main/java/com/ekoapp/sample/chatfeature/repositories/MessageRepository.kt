package com.ekoapp.sample.chatfeature.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import com.google.gson.JsonObject
import io.reactivex.Completable
import javax.inject.Inject

class MessageRepository @Inject constructor() {

    fun unFlag(messageId: String): Completable = EkoClient.newMessageRepository().report(messageId).unflag()

    fun flag(messageId: String): Completable = EkoClient.newMessageRepository().report(messageId).flag()

    fun setTags(messageId: String, tags: EkoTags): Completable {
        return EkoClient.newMessageRepository().setTags(messageId, tags)
    }

    fun getMessageCollectionByTags(channelId: String,
                                   parentId: String?,
                                   includingTags: EkoTags,
                                   excludingTags: EkoTags,
                                   stackFromEnd: Boolean): LiveData<PagedList<EkoMessage>> {
        return EkoClient.newMessageRepository().getMessageCollectionByTags(channelId,
                parentId,
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

    fun customMessage(channelId: String, parentId: String? = null, data: JsonObject): Completable {
        val builder = EkoClient.newMessageRepository().createMessage(channelId).custom(data)
        return if (parentId.isNullOrEmpty())
            builder.build()
                    .send()
        else builder.parentId(parentId)
                .build()
                .send()
    }

}
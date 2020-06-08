package com.ekoapp.sample.chatfeature.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.sample.chatfeature.data.SendMessageData
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

    fun textMessage(data: SendMessageData): Completable {
        data.apply {
            return EkoClient.newMessageRepository().createMessage(channelId)
                    .text(text)
                    .parentId(parentId)
                    .build()
                    .send()
        }
    }

    fun customMessage(data: SendMessageData): Completable {
        val metadata = JsonObject()
        data.apply {
            custom?.map { metadata.addProperty(it.key, it.value); }
            return EkoClient.newMessageRepository().createMessage(channelId)
                    .custom(metadata)
                    .parentId(parentId)
                    .build()
                    .send()
        }
    }

    fun fileMessage(data: SendMessageData): Completable {
        data.apply {
            return EkoClient.newMessageRepository().createMessage(channelId)
                    .file(file)
                    .parentId(parentId)
                    .build()
                    .send()
        }
    }

    fun imageMessage(data: SendMessageData): Completable {
        data.apply {
            return EkoClient.newMessageRepository().createMessage(channelId)
                    .image(image)
                    .parentId(parentId)
                    .build()
                    .send()
        }
    }

}
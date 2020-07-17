package com.ekoapp.sample.chatfeature.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.ekosdk.internal.data.model.EkoMessageReaction
import com.ekoapp.sample.chatfeature.data.ChannelData
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

    fun getMessageCollectionByTags(data: ChannelData): LiveData<PagedList<EkoMessage>> {
        data.apply {
            return EkoClient.newMessageRepository().getMessageCollectionByTags(
                    channelId,
                    parentId,
                    includingTags,
                    excludingTags,
                    stackFromEnd)
        }
    }

    fun getMessageReactionCollection(messageId: String): LiveData<PagedList<EkoMessageReaction>> {
        return EkoClient.newMessageRepository().getMessageReactionCollection(messageId)
    }

    fun textMessage(data: SendMessageData): Completable {
        data.apply {
            return if (data.messageId == null) {
                EkoClient.newMessageRepository().createMessage(channelId)
                        .text(text)
                        .parentId(data.parentId)
                        .build()
                        .send()
            } else {
                EkoClient.newMessageRepository().createMessage(channelId)
                        .text(text)
                        .parentId(data.messageId)
                        .build()
                        .send()
            }
        }
    }

    fun customMessage(data: SendMessageData): Completable {
        val metadata = JsonObject()
        data.apply {
            custom?.map { metadata.addProperty(it.key, it.value); }
            return if (data.messageId == null) {
                EkoClient.newMessageRepository().createMessage(channelId)
                        .custom(metadata)
                        .parentId(data.parentId)
                        .build()
                        .send()
            } else {
                EkoClient.newMessageRepository().createMessage(channelId)
                        .custom(metadata)
                        .parentId(data.messageId)
                        .build()
                        .send()
            }
        }
    }

    fun fileMessage(data: SendMessageData): Completable {
        data.apply {
            return if (data.messageId == null) {
                EkoClient.newMessageRepository().createMessage(channelId)
                        .file(file)
                        .parentId(data.parentId)
                        .build()
                        .send()
            } else {
                EkoClient.newMessageRepository().createMessage(channelId)
                        .file(file)
                        .parentId(data.messageId)
                        .build()
                        .send()
            }
        }
    }

    fun imageMessage(data: SendMessageData): Completable {
        data.apply {
            return if (data.messageId == null) {
                EkoClient.newMessageRepository().createMessage(channelId)
                        .image(image)
                        .parentId(data.parentId)
                        .build()
                        .send()
            } else {
                EkoClient.newMessageRepository().createMessage(channelId)
                        .image(image)
                        .parentId(data.messageId)
                        .build()
                        .send()
            }
        }
    }

}
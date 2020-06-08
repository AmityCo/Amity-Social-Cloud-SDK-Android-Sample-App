package com.ekoapp.sample.chatfeature.repositories

import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoTags
import io.reactivex.Completable
import javax.inject.Inject

class MessageRepository @Inject constructor() {

    fun unFlag(messageId: String): Completable = EkoClient.newMessageRepository().report(messageId).unflag()

    fun flag(messageId: String): Completable = EkoClient.newMessageRepository().report(messageId).flag()

    fun setTags(messageId: String, tags: EkoTags): Completable {
        return EkoClient.newMessageRepository().setTags(messageId, tags)
    }
}
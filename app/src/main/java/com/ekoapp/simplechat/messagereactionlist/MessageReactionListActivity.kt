package com.ekoapp.simplechat.messagereactionlist

import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.reaction.EkoReaction
import com.ekoapp.simplechat.intent.OpenMessageReactionListIntent
import io.reactivex.Flowable

class MessageReactionListActivity : ReactionListActivity() {

    override fun getReactionCollection(): Flowable<PagedList<EkoReaction>> {
        val message = OpenMessageReactionListIntent.getMessage(intent)
        return EkoClient.newMessageRepository().getReactionCollection(message.getMessageId()).build().query()
    }
}
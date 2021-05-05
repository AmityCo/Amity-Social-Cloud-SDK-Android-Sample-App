package com.amity.sample.ascsdk.reaction

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.core.reaction.AmityReaction
import com.amity.sample.ascsdk.intent.OpenMessageReactionListIntent
import io.reactivex.Flowable


class MessageReactionListActivity : ReactionListActivity() {

    override fun getReactionCollection(): Flowable<PagedList<AmityReaction>> {
        val message = OpenMessageReactionListIntent.getMessage(intent)
        return AmityChatClient.newMessageRepository().getReactions(message.getMessageId()).build().query()
    }

}
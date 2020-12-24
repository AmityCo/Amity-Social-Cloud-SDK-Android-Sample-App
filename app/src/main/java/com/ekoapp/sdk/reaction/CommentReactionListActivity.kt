package com.ekoapp.sdk.reaction

import androidx.paging.PagedList
import com.ekoapp.ekosdk.reaction.EkoReaction
import com.ekoapp.sdk.intent.OpenCommentReactionListIntent
import io.reactivex.Flowable


class CommentReactionListActivity : ReactionListActivity() {

    override fun getReactionCollection(): Flowable<PagedList<EkoReaction>> {
        val comment = OpenCommentReactionListIntent.getComment(intent)
        return comment.getReactionCollection()
                .build()
                .query()
    }

}
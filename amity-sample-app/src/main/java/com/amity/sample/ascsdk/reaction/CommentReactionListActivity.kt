package com.amity.sample.ascsdk.reaction

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.core.reaction.AmityReaction
import com.amity.sample.ascsdk.intent.OpenCommentReactionListIntent
import io.reactivex.Flowable


class CommentReactionListActivity : ReactionListActivity() {

    override fun getReactionCollection(): Flowable<PagedList<AmityReaction>> {
        val comment = OpenCommentReactionListIntent.getComment(intent)
        return comment.getReactions()
                .build()
                .query()
    }

}
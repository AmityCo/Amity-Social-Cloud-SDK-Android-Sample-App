package com.ekoapp.simplechat.comment

import androidx.paging.PagedList
import com.ekoapp.ekosdk.reaction.EkoReaction
import com.ekoapp.simplechat.intent.OpenCommentReactionListIntent
import com.ekoapp.simplechat.messagereactionlist.ReactionListActivity
import io.reactivex.Flowable

class CommentReactionListActivity : ReactionListActivity() {

    override fun getReactionCollection(): Flowable<PagedList<EkoReaction>> {
        val comment = OpenCommentReactionListIntent.getComment(intent)
        return comment.getReactionCollection()
                .build()
                .query()
    }

}
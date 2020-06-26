package com.ekoapp.sample.socialfeature.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.ekosdk.EkoUserFeedSortOption
import com.ekoapp.ekosdk.internal.data.model.EkoPostReaction
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.ReactionData
import com.ekoapp.sample.socialfeature.users.data.UserData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

class FeedRepository @Inject constructor() {

    fun getUserFeed(data: UserData): LiveData<PagedList<EkoPost>> {
        return EkoClient.newFeedRepository().getUserFeed(data.userId, EkoUserFeedSortOption.LAST_CREATED)
    }

    fun createPost(userId: String, description: String): Completable {
        return EkoClient.newFeedRepository().createPost()
                .targetUser(userId)
                .text(description)
                .build()
                .post()
    }

    fun getPost(postId: String): Flowable<EkoPost> {
        return EkoClient.newFeedRepository().getPost(postId)
    }

    fun editPost(postId: String, description: String): Completable {
        return EkoClient.newFeedRepository().editPost(postId)
                .text(description)
                .apply()
    }

    fun deletePost(item: EkoPost): Completable {
        return EkoClient.newFeedRepository().editPost(item.postId)
                .delete()
    }

    fun reportPost(item: EkoPost): Completable {
        return EkoClient.newFeedRepository().report(item.postId)
                .flag()
    }

    fun cancelReportPost(item: EkoPost): Completable {
        return EkoClient.newFeedRepository().report(item.postId)
                .unflag()
    }

    fun getPostReactionCollection(postId: String): LiveData<PagedList<EkoPostReaction>> {
        return EkoClient.newFeedRepository().getPostReactionCollection(postId)
    }

    fun getPostReactionCollectionByReactionName(postId: String, reactionName: String): LiveData<PagedList<EkoPostReaction>> {
        return EkoClient.newFeedRepository().getPostReactionCollectionByReactionName(postId, reactionName)
    }

    fun removeReaction(item: ReactionData): Completable {
        return item.item
                .react()
                .removeReaction(item.text)
    }

    fun addReaction(item: ReactionData): Completable {
        return item.item
                .react()
                .addReaction(item.text)
    }

    fun myReaction(item: ReactionData): Maybe<EkoPostReaction> {
        return item.item
                .react()
                .myReaction(item.text)
    }

}
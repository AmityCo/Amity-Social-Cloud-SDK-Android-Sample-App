package com.ekoapp.sample.socialfeature.userfeeds.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.ekosdk.internal.data.model.EkoPostReaction
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.repository.FeedRepository
import com.ekoapp.sample.socialfeature.repository.UserRepository
import com.ekoapp.sample.socialfeature.userfeeds.data.FeedsData
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.ReactionData
import com.ekoapp.sample.socialfeature.users.data.UserData
import timber.log.Timber
import javax.inject.Inject

class UserFeedsViewModel @Inject constructor(private val feedRepository: FeedRepository,
                                             private val userRepository: UserRepository) : DisposableViewModel() {
    private lateinit var userDataIntent: UserData
    private lateinit var feedsDataIntent: FeedsData

    //TODO refactor make it private
    val createFeedsActionRelay = SingleLiveData<UserData>()
    val editFeedsActionRelay = SingleLiveData<EditUserFeedsData>()
    val findUsersActionRelay = SingleLiveData<Unit>()
    val usersActionRelay = SingleLiveData<UserData>()
    val seeAllUsersActionRelay = SingleLiveData<Unit>()
    val reactionsSummaryActionRelay = SingleLiveData<UserReactionData>()
    val myReactionRelay = MutableLiveData<EkoPostReaction>()

    fun observeCreateFeedsPage(): SingleLiveData<UserData> = createFeedsActionRelay
    fun observeEditFeedsPage(): SingleLiveData<EditUserFeedsData> = editFeedsActionRelay
    fun observeFindUsersPage(): SingleLiveData<Unit> = findUsersActionRelay
    fun observeUserPage(): SingleLiveData<UserData> = usersActionRelay
    fun observeSeeAllUsersPage(): SingleLiveData<Unit> = seeAllUsersActionRelay
    fun observeReactionsSummaryPage(): SingleLiveData<UserReactionData> = reactionsSummaryActionRelay
    fun observeMyReaction(): LiveData<EkoPostReaction> = myReactionRelay

    fun setupIntent(data: UserData?) {
        userDataIntent = data ?: UserData(userId = EkoClient.getUserId())
    }

    fun getIntentUserData(actionRelay: (UserData) -> Unit): UserData {
        userDataIntent.let(actionRelay::invoke)
        return userDataIntent
    }

    fun setupIntent(data: FeedsData?) {
        feedsDataIntent = data ?: FeedsData(postId = "")
    }

    fun getIntentFeedsData(actionRelay: (FeedsData) -> Unit): FeedsData {
        feedsDataIntent.let(actionRelay::invoke)
        return feedsDataIntent
    }

    fun bindUserFeeds(data: UserData): LiveData<PagedList<EkoPost>> = feedRepository.getUserFeed(data)

    fun bindUsers(): LiveData<PagedList<EkoUser>> = userRepository.getAllUsers()

    fun bindDeletePost(item: EkoPost) {
        feedRepository
                .deletePost(item)
                .subscribe()
    }

    fun bindMyReaction(item: ReactionData) {
        feedRepository
                .myReaction(item)
                .doOnSuccess(myReactionRelay::postValue)
                .doOnError {
                    Timber.d("${getCurrentClassAndMethodNames()}${it.message}")
                }
                .subscribe()
    }

    fun reactionFeeds(item: ReactionData) = if (item.isChecked) bindAddReaction(item) else bindRemoveReaction(item)

    private fun bindAddReaction(item: ReactionData) {
        feedRepository
                .addReaction(item)
                .subscribe()
    }

    private fun bindRemoveReaction(item: ReactionData) {
        feedRepository
                .removeReaction(item)
                .subscribe()
    }

}
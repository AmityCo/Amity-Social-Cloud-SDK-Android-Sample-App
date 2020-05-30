package com.ekoapp.sample.socialfeature.userfeeds.view

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.*
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.userfeeds.data.FeedsData
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.ReactionData
import com.ekoapp.sample.socialfeature.users.data.UserData
import javax.inject.Inject

class UserFeedsViewModel @Inject constructor() : DisposableViewModel() {
    private lateinit var userDataIntent: UserData
    private lateinit var feedsDataIntent: FeedsData

    val createFeedsActionRelay = SingleLiveData<UserData>()
    val editFeedsActionRelay = SingleLiveData<EditUserFeedsData>()
    val findUsersActionRelay = SingleLiveData<Unit>()
    val usersActionRelay = SingleLiveData<UserData>()
    val seeAllUsersActionRelay = SingleLiveData<Unit>()
    val reactionsSummaryActionRelay = SingleLiveData<UserReactionData>()

    fun observeCreateFeedsPage(): SingleLiveData<UserData> = createFeedsActionRelay
    fun observeEditFeedsPage(): SingleLiveData<EditUserFeedsData> = editFeedsActionRelay
    fun observeFindUsersPage(): SingleLiveData<Unit> = findUsersActionRelay
    fun observeUserPage(): SingleLiveData<UserData> = usersActionRelay
    fun observeSeeAllUsersPage(): SingleLiveData<Unit> = seeAllUsersActionRelay
    fun observeReactionsSummaryPage(): SingleLiveData<UserReactionData> = reactionsSummaryActionRelay

    fun bindUserFeeds(data: UserData): LiveData<PagedList<EkoPost>> {
        return EkoClient.newFeedRepository().getUserFeed(data.userId, EkoUserFeedSortOption.LAST_CREATED)
    }

    fun bindUserList(): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().getAllUsers(EkoUserSortOption.DISPLAYNAME)
    }

    fun deletePost(item: EkoPost) {
        EkoClient.newFeedRepository().editPost(item.postId)
                .delete()
                .subscribe()
    }

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

    fun reactionFeeds(item: ReactionData) = if (item.isChecked) addReaction(item) else removeReaction(item)

    private fun removeReaction(item: ReactionData) {
        item.item.react()
                .removeReaction(item.text)
                .subscribe()
    }

    private fun addReaction(item: ReactionData) {
        item.item.react()
                .addReaction(item.text)
                .subscribe()
    }
}
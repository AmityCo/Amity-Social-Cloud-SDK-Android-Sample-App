package com.ekoapp.sample.socialfeature.userfeeds.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.*
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.socialfeature.constants.UPPERMOST
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.ReactionData
import com.ekoapp.sample.socialfeature.users.data.UserData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

sealed class UserFeedsViewSeal {
    class GetUserFeeds(val data: LiveData<PagedList<EkoPost>>) : UserFeedsViewSeal()
    class CreateUserFeeds(val scrollToPosition: Int = UPPERMOST) : UserFeedsViewSeal()
}

class UserFeedsViewModel @Inject constructor() : DisposableViewModel() {
    private val feedsRelay = PublishProcessor.create<Unit>()
    private lateinit var userDataIntent: UserData
    private val userFeeds = MutableLiveData<UserFeedsViewSeal>()

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

    fun bindUserFeedsSeal(data: UserData): LiveData<UserFeedsViewSeal> {
        userFeeds.postValue(UserFeedsViewSeal.GetUserFeeds(data = getUserFeeds(data)))
        feedsRelay
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    userFeeds.postValue(UserFeedsViewSeal.CreateUserFeeds())
                }
                .subscribe() into disposables
        return userFeeds
    }

    private fun getUserFeeds(data: UserData) = EkoClient.newFeedRepository().getUserFeed(data.userId, EkoUserFeedSortOption.LAST_CREATED)

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

    fun updateFeeds() {
        feedsRelay.onNext(Unit)
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
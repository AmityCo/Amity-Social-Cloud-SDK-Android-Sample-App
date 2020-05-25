package com.ekoapp.sample.socialfeature.userfeed.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.*
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.core.ui.extensions.toLiveData
import com.ekoapp.sample.socialfeature.constants.UPPERMOST
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import com.ekoapp.sample.socialfeature.userfeed.view.renders.ReactionData
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
    private var userDataIntent: UserData? = null
    private val userFeeds = MutableLiveData<UserFeedsViewSeal>()
    private val actionLikeRelay = PublishProcessor.create<Boolean>()

    val createFeedsActionRelay = SingleLiveData<Unit>()
    val editFeedsActionRelay = SingleLiveData<EditUserFeedsData>()
    val findUsersActionRelay = SingleLiveData<Unit>()
    val seeAllUsersActionRelay = SingleLiveData<Unit>()

    fun observeCreateFeedsPage(): SingleLiveData<Unit> = createFeedsActionRelay
    fun observeEditFeedsPage(): SingleLiveData<EditUserFeedsData> = editFeedsActionRelay
    fun observeFindUsersPage(): SingleLiveData<Unit> = findUsersActionRelay
    fun observeSeeAllUsersPage(): SingleLiveData<Unit> = seeAllUsersActionRelay
    fun observeActionLikeRelay() = actionLikeRelay.toLiveData()

    fun getIntentUserData(actionRelay: (UserData) -> Unit) {
        userDataIntent?.let(actionRelay::invoke)
    }

    fun getMyProfile() = UserData(userId = EkoClient.getUserId())

    fun executeUserFeeds(data: UserData): LiveData<UserFeedsViewSeal> {
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

    fun getUserList(): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().getAllUsers(EkoUserSortOption.DISPLAYNAME)
    }

    fun deletePost(item: EkoPost) {
        EkoClient.newFeedRepository().editPost(item.postId)
                .delete()
                .subscribe()
    }

    fun setupIntent(data: UserData?) {
        userDataIntent = data
    }

    fun updateFeeds() {
        feedsRelay.onNext(Unit)
    }

    fun likeFeeds(item: ReactionData) = if (item.isChecked) addReaction(item) else removeReaction(item)

    private fun removeReaction(item: ReactionData) {
        item.item.react()
                .removeReaction(item.text)
                .doOnSubscribe {
                    actionLikeRelay.onNext(false)
                }
                .doOnError {
                    actionLikeRelay.onNext(true)
                }
                .subscribe()
    }

    private fun addReaction(item: ReactionData) {
        item.item.react()
                .addReaction(item.text)
                .doOnSubscribe {
                    actionLikeRelay.onNext(true)
                }
                .doOnError {
                    actionLikeRelay.onNext(false)
                }
                .subscribe()
    }
}
package com.ekoapp.sample.socialfeature.userfeed.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.*
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.rx.into
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.socialfeature.constants.UPPERMOST
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
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
    val editFeedsActionRelay = SingleLiveData<EditUserFeedsData>()

    fun observeEditFeedsPage(): SingleLiveData<EditUserFeedsData> = editFeedsActionRelay

    fun getIntentUserData(actionRelay: (UserData) -> Unit) {
        userDataIntent?.let(actionRelay::invoke)
    }

    fun getMyProfile() = UserData(userId = EkoClient.getUserId())

    fun bindUserFeeds(data: UserData): LiveData<UserFeedsViewSeal> {
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
}
package com.ekoapp.sample.socialfeature.userfeeds.view

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.ekosdk.internal.data.model.EkoPostReaction
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.enums.ReportTypes
import com.ekoapp.sample.core.preferences.PreferenceHelper
import com.ekoapp.sample.core.preferences.PreferenceHelper.report
import com.ekoapp.sample.core.seals.ReportSealType
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.core.ui.extensions.toLiveData
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.repositories.FeedRepository
import com.ekoapp.sample.socialfeature.repositories.UserRepository
import com.ekoapp.sample.socialfeature.userfeeds.data.FeedsData
import com.ekoapp.sample.socialfeature.userfeeds.view.renders.ReactionData
import com.ekoapp.sample.socialfeature.users.data.UserData
import timber.log.Timber
import javax.inject.Inject

class UserFeedsViewModel @Inject constructor(private val context: Context,
                                             private val feedRepository: FeedRepository,
                                             private val userRepository: UserRepository) : DisposableViewModel() {

    private val prefs: SharedPreferences = PreferenceHelper.defaultPreference(context)
    private val feedsByIdActionRelay = SingleLiveData<FeedsData>()
    private val deleteFeedsRelay = MutableLiveData<Unit>()
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
    fun observeFeedsByIdPage(): SingleLiveData<FeedsData> = feedsByIdActionRelay
    fun observeFindUsersPage(): SingleLiveData<Unit> = findUsersActionRelay
    fun observeUserPage(): SingleLiveData<UserData> = usersActionRelay
    fun observeSeeAllUsersPage(): SingleLiveData<Unit> = seeAllUsersActionRelay
    fun observeReactionsSummaryPage(): SingleLiveData<UserReactionData> = reactionsSummaryActionRelay
    fun observeDeleteFeeds(): LiveData<Unit> = deleteFeedsRelay
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

    fun renderFeedsById(data: FeedsData) {
        feedsByIdActionRelay.postValue(data)
    }

    fun bindUserFeeds(data: UserData): LiveData<PagedList<EkoPost>> = feedRepository.getUserFeed(data)

    fun bindUsers(): LiveData<PagedList<EkoUser>> = userRepository.getAllUsers()

    fun bindGetPost(data: FeedsData): LiveData<EkoPost> = feedRepository.getPost(data.postId).toLiveData()

    fun bindDeletePost(item: EkoPost) {
        feedRepository
                .deletePost(item)
                .doOnComplete {
                    deleteFeedsRelay.postValue(Unit)
                }
                .subscribe()
    }

    fun initReportPost(type: ReportSealType) {
        when (type) {
            is ReportSealType.FLAG -> {
                bindReportPost(type.item)
            }
            is ReportSealType.UNFLAG -> {
                bindCancelReportPost(type.item)
            }
        }
    }

    private fun bindReportPost(item: EkoPost) {
        feedRepository
                .reportPost(item)
                .doOnComplete { prefs.report = ReportTypes.FLAG.text }
                .subscribe()
    }

    private fun bindCancelReportPost(item: EkoPost) {
        feedRepository
                .cancelReportPost(item)
                .doOnComplete { prefs.report = ReportTypes.UNFLAG.text }
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
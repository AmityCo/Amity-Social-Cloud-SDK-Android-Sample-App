package com.amity.sample.ascsdk.userlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenMyUserFollowerListIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import com.amity.socialcloud.sdk.core.user.AmityFollowStatus
import com.amity.socialcloud.sdk.core.user.AmityFollowStatusFilter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_follow_list.*

class MyUserFollowerListActivity : AppCompatActivity() {
    private val userRepository = AmityCoreClient.newUserRepository()
    private val adapter = UserFollowerListAdapter()
    private var followers: LiveData<PagedList<AmityFollowRelationship>>? = null
    private var followStatus: String = ""

    @ExperimentalPagingApi
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_follow_list)

        followStatus = OpenMyUserFollowerListIntent.getFollowStatus(intent) ?: ""

        observeUserFollowerCollection()
        setUpListeners()
    }

    @ExperimentalPagingApi
    private fun observeUserFollowerCollection() {
        followers?.removeObservers(this)
        user_list_recyclerview.adapter = adapter

        followers = getUserFollowersLiveData()
        followers?.observe(this, Observer<PagedList<AmityFollowRelationship>> {
            adapter.submitList(it)
        })
    }

    @ExperimentalPagingApi
    private fun getUserFollowersLiveData(): LiveData<PagedList<AmityFollowRelationship>> {
        return LiveDataReactiveStreams.fromPublisher(
                userRepository.relationship()
                        .me()
                        .getFollowers()
                        .status(AmityFollowStatusFilter.enumOf(followStatus))
                        .build()
                        .query()
        )
    }

    private fun setUpListeners() {
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {

                }, Consumer { })

        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { userFollowStatus ->
                    when (userFollowStatus.getStatus()) {
                        AmityFollowStatus.PENDING -> {
                            val acceptRequest = "Accept request"
                            val declineRequest = "Decline request"
                            MaterialDialog(this).show {
                                listItems(items = listOf(acceptRequest, declineRequest)) { dialog, position, text ->
                                    when (text.toString()) {
                                        acceptRequest -> {
                                            accept(userFollowStatus)
                                        }
                                        declineRequest -> {
                                            decline(userFollowStatus)
                                        }
                                    }
                                }
                            }
                        }
                        AmityFollowStatus.ACCEPTED -> {
                            MaterialDialog(this).show {
                                positiveButton(text = "Remove follower") {
                                    remove(userFollowStatus)
                                }
                            }
                        }
                        else -> {
                        }
                    }
                }, Consumer { })
    }

    private fun accept(followRelationship: AmityFollowRelationship) {
        followRelationship.getSourceUser()?.getUserId()?.let { userId ->
            userRepository.relationship()
                    .me()
                    .accept(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
    }

    private fun decline(followRelationship: AmityFollowRelationship) {
        followRelationship.getSourceUser()?.getUserId()?.let { userId ->
            userRepository.relationship()
                    .me()
                    .decline(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
    }

    private fun remove(followRelationship: AmityFollowRelationship) {
        followRelationship.getSourceUser()?.getUserId()?.let { userId ->
            userRepository.relationship()
                    .me()
                    .removeFollower(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
    }
}



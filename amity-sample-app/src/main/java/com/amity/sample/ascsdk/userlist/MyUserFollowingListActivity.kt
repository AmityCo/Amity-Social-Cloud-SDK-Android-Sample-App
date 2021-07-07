package com.amity.sample.ascsdk.userlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenMyUserFollowingListIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import com.amity.socialcloud.sdk.core.user.AmityFollowStatus
import com.amity.socialcloud.sdk.core.user.AmityFollowStatusFilter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_follow_list.*

class MyUserFollowingListActivity : AppCompatActivity() {

    private val userRepository = AmityCoreClient.newUserRepository()
    private val adapter = UserFollowingListAdapter()
    private var followings: LiveData<PagedList<AmityFollowRelationship>>? = null
    private var followStatus: String = ""

    @ExperimentalPagingApi
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_follow_list)

        followStatus = OpenMyUserFollowingListIntent.getFollowStatus(intent) ?: ""

        observeUserFollowingCollection()
        setUpListeners()
    }

    @ExperimentalPagingApi
    private fun observeUserFollowingCollection() {
        followings?.removeObservers(this)
        user_list_recyclerview.adapter = adapter

        followings = getUserFollowingsLiveData()
        followings?.observe(this, Observer<PagedList<AmityFollowRelationship>> {
            adapter.submitList(it)
        })
    }

    @ExperimentalPagingApi
    private fun getUserFollowingsLiveData(): LiveData<PagedList<AmityFollowRelationship>> {
        return LiveDataReactiveStreams.fromPublisher(
                userRepository.relationship()
                        .me()
                        .getFollowings()
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
                            showActionDialog("Cancel request", userFollowStatus)
                        }
                        AmityFollowStatus.ACCEPTED -> {
                            showActionDialog("Unfollow", userFollowStatus)
                        }
                        else -> {
                        }
                    }
                }, Consumer { })
    }

    private fun showActionDialog(status: String, followRelationship: AmityFollowRelationship) {
        MaterialDialog(this).show {
            positiveButton(text = status) {
                unfollow(followRelationship)
            }
        }
    }

    private fun unfollow(followRelationship: AmityFollowRelationship) {
        followRelationship.getTargetUser()?.getUserId()?.let { userId ->
            userRepository.relationship()
                    .me()
                    .unfollow(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
    }
}



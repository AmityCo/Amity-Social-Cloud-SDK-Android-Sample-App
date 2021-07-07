package com.amity.sample.ascsdk.userlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenUserFollowingListIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_follow_list.*

class UserFollowingListActivity : AppCompatActivity() {
    private val userRepository = AmityCoreClient.newUserRepository()
    private val adapter = UserFollowingListAdapter()
    private var followings: LiveData<PagedList<AmityFollowRelationship>>? = null

    lateinit var userId: String

    @ExperimentalPagingApi
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_follow_list)

        userId = OpenUserFollowingListIntent.getUserId(intent)
        observeUserCollection()
        setUpListeners()
    }

    @ExperimentalPagingApi
    private fun observeUserCollection() {
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
                        .user(userId)
                        .getFollowings()
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

                }, Consumer { })
    }
}



package com.amity.sample.ascsdk.userlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenUserFollowerListIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_follow_list.*

class UserFollowerListActivity : AppCompatActivity() {
    private val userRepository = AmityCoreClient.newUserRepository()
    private val adapter = UserFollowerListAdapter()
    private var followers: LiveData<PagedList<AmityFollowRelationship>>? = null
    lateinit var userId: String

    @ExperimentalPagingApi
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_follow_list)
        // state for paging 3
//        adapter.addLoadStateListener { loadStates ->
//            val refreshState = loadStates.mediator?.refresh
//            when (refreshState) {
//                is LoadState.Error -> {
//                    if (AmityError.from(refreshState.error) == AmityError.PERMISSION_DENIED) {
//                        user_list_recyclerview.visibility = View.GONE
//                        Toast.makeText(this, "Feed is private", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                is LoadState.NotLoading -> {
//                    if (adapter.itemCount == 0) {
//                        // show empty state
//                    }
//                }
//                is LoadState.Loading -> {
//                    // show loading state
//                }
//            }
//        }
        userId = OpenUserFollowerListIntent.getUserId(intent)
        observeUserFollowerCollection()
        setUpListeners()

    }

    @ExperimentalPagingApi
    private fun observeUserFollowerCollection() {
        followers?.removeObservers(this)
        user_list_recyclerview.adapter = adapter
        user_list_recyclerview.visibility = View.VISIBLE

        followers = getUserFollowersLiveData()
        followers?.observe(this, Observer<PagedList<AmityFollowRelationship>> {
            adapter.submitList(it)
        })
    }

    @ExperimentalPagingApi
    private fun getUserFollowersLiveData(): LiveData<PagedList<AmityFollowRelationship>> {
        return LiveDataReactiveStreams.fromPublisher(
                userRepository.relationship()
                        .user(userId)
                        .getFollowers()
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



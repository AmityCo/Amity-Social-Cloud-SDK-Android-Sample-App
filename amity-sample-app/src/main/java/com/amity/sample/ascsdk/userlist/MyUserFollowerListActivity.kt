package com.amity.sample.ascsdk.userlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenMyUserFollowerListIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import com.amity.socialcloud.sdk.core.user.AmityFollowStatus
import com.amity.socialcloud.sdk.core.user.AmityFollowStatusFilter
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_follow_list.*

class MyUserFollowerListActivity : AppCompatActivity() {
    private val userRepository = AmityCoreClient.newUserRepository()

    private val disposable = CompositeDisposable()

    private var followStatus: String = ""

    private val pagedListAdapter = FollowerPagedListAdapter()
    private val pagingDataAdapter = FollowerPagingDataAdapter()

    private var isPagedListAdapter = true

    @ExperimentalPagingApi
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_follow_list)

        followStatus = OpenMyUserFollowerListIntent.getFollowStatus(intent) ?: ""
        usePagedListAdapter()
        setUpListeners()
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    @ExperimentalPagingApi
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val changeAdapterId = 101
        val pagingDataTitle = "Change to PagingData"
        val pagedListTitle = "Change to PagedList"

        if (menu?.findItem(changeAdapterId) == null) {
            val changeAdapter = menu?.add(Menu.NONE, changeAdapterId, 0, pagingDataTitle)
            changeAdapter?.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)

            changeAdapter?.setOnMenuItemClickListener {
                if (isPagedListAdapter) {
                    usePagingDataAdapter()
                } else {
                    usePagedListAdapter()
                }
                true
            }
        } else {
            val changeToAdapter = if (isPagedListAdapter) {
                pagingDataTitle
            } else {
                pagedListTitle
            }
            menu.findItem(changeAdapterId).title = changeToAdapter
        }
        super.onPrepareOptionsMenu(menu)
        return true
    }

    private fun usePagedListAdapter() {
        disposable.clear()
        isPagedListAdapter = true
        user_list_recyclerview.swapAdapter(pagedListAdapter, true)

        getFollowersAndSubmitPagedListAdapter {}
    }

    @ExperimentalPagingApi
    private fun usePagingDataAdapter() {
        disposable.clear()
        isPagedListAdapter = false
        user_list_recyclerview.swapAdapter(pagingDataAdapter, true)

        getFollowersAndSubmitPagingDataAdapter {}
    }

    private fun getFollowersAndSubmitPagedListAdapter(callback: (PagedList<AmityFollowRelationship>) -> Unit = {}) {
        disposable.add(getFollowersAsPagedList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    pagedListAdapter.submitList(it)
                    callback.invoke(it)
                }
                .doOnError(Throwable::printStackTrace)
                .subscribe())
    }

    @ExperimentalPagingApi
    private fun getFollowersAndSubmitPagingDataAdapter(callback: (PagingData<AmityFollowRelationship>) -> Unit = {}) {
        disposable.add(getFollowersAsPagingData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    pagingDataAdapter.submitData(lifecycle, it)
                    callback.invoke(it)
                }
                .doOnError(Throwable::printStackTrace)
                .subscribe())
    }

    private fun getFollowersAsPagedList(): Flowable<PagedList<AmityFollowRelationship>> {
        return userRepository.relationship()
                .me()
                .getFollowers()
                .status(AmityFollowStatusFilter.enumOf(followStatus))
                .build()
                .query()
    }

    @ExperimentalPagingApi
    private fun getFollowersAsPagingData(): Flowable<PagingData<AmityFollowRelationship>> {
        return userRepository.relationship()
                .me()
                .getFollowers()
                .status(AmityFollowStatusFilter.enumOf(followStatus))
                .build()
                .getPagingData()
    }

    private fun checkFollowStatus(userFollowStatus: AmityFollowRelationship) {
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

    private fun setUpListeners() {
        pagedListAdapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { userFollowStatus ->
                    checkFollowStatus(userFollowStatus)
                }, Consumer { })

        pagingDataAdapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { userFollowStatus ->
                    checkFollowStatus(userFollowStatus)
                }, Consumer { })
    }

}



package com.amity.sample.ascsdk.userlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenUserFollowingListIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_follow_list.*

class UserFollowingListActivity : AppCompatActivity() {
    private val userRepository = AmityCoreClient.newUserRepository()

    private val disposable = CompositeDisposable()

    lateinit var userId: String

    private val pagedListAdapter = FollowingPagedListAdapter()
    private val pagingDataAdapter = FollowingPagingDataAdapter()

    private var isPagedListAdapter = true

    @ExperimentalPagingApi
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_follow_list)

        userId = OpenUserFollowingListIntent.getUserId(intent)
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

        getFollowingsAndSubmitPagedListAdapter {}
    }

    @ExperimentalPagingApi
    private fun usePagingDataAdapter() {
        disposable.clear()
        isPagedListAdapter = false
        user_list_recyclerview.swapAdapter(pagingDataAdapter, true)

        getFollowingsAndSubmitPagingDataAdapter {}
    }

    private fun getFollowingsAndSubmitPagedListAdapter(callback: (PagedList<AmityFollowRelationship>) -> Unit = {}) {
        disposable.add(getFollowingAsPagedList()
                .doOnNext {
                    pagedListAdapter.submitList(it)
                    callback.invoke(it)
                }
                .doOnError(Throwable::printStackTrace)
                .subscribe())
    }

    @ExperimentalPagingApi
    private fun getFollowingsAndSubmitPagingDataAdapter(callback: (PagingData<AmityFollowRelationship>) -> Unit = {}) {
        disposable.add(getFollowingsAsPagingData()
                .doOnNext {
                    pagingDataAdapter.submitData(lifecycle, it)
                    callback.invoke(it)
                }
                .doOnError(Throwable::printStackTrace)
                .subscribe())
    }

    private fun getFollowingAsPagedList(): Flowable<PagedList<AmityFollowRelationship>> {
        return userRepository.relationship()
                .user(userId)
                .getFollowings()
                .build()
                .query()
    }

    @ExperimentalPagingApi
    private fun getFollowingsAsPagingData(): Flowable<PagingData<AmityFollowRelationship>> {
        return userRepository.relationship()
                .user(userId)
                .getFollowings()
                .build()
                .getPagingData()
    }

    private fun setUpListeners() {
        pagedListAdapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { userFollowStatus ->

                }, Consumer { })

        pagingDataAdapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { userFollowStatus ->

                }, Consumer { })
    }
}



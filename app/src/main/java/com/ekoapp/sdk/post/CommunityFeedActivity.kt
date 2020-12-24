package com.ekoapp.sdk.post

import android.os.Bundle
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.ekoapp.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.community.EkoCommunity
import com.ekoapp.ekosdk.feed.EkoPost
import com.ekoapp.ekosdk.feed.query.EkoCommunityFeedSortOption
import com.ekoapp.ekosdk.permission.EkoPermission
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.intent.OpenCommunityFeedIntent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CommunityFeedActivity : PostListActivity() {

    lateinit var community: EkoCommunity
    private var checkedItem = 0

    override val targetType: String
        get() = "community"

    override val targetId: String
        get() = community.getCommunityId()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        community = OpenCommunityFeedIntent.getCommunity(intent)
        super.onCreate(savedInstanceState)
    }

    override fun inflateMenu(menuRes: (Int) -> Unit) {
        menuRes.invoke(R.menu.menu_post_list)
    }

    override fun checkPermission() {
        showDialog(R.string.check_permission, "", EkoPermission.EDIT_USER.value, false) { dialog, input ->
            val inputStr = input.toString()
            if (EkoPermission.values().any { it.value == inputStr }) {
                val disposable = EkoClient
                        .hasPermission(EkoPermission.valueOf(inputStr))
                        .atCommunity(community.getCommunityId())
                        .check()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            showToast("You have permission or not?: $it")
                        }
                        .doOnError { Timber.d("${getCurrentClassAndMethodNames()}${it.message}") }
                        .subscribe()
                compositeDisposable.addAll(disposable)
            } else {
                showToast("Your permission is invalid")
            }
        }
    }

    override fun getPostCollection(): Flowable<PagedList<EkoPost>> {
        return feedRepository
                .getCommunityFeed(community.getCommunityId())
                .build()
                .query()
    }

    override fun selectSortOption(callback: (Int) -> Unit) {
        val options = EkoCommunityFeedSortOption.values().map { Pair(it.apiKey, false) }
        val choices = options.map { it.first }.toTypedArray()
        val checkedChoices = options.map { it.second }.toBooleanArray()
        checkedChoices[checkedItem] = true
        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.select_item)
                .setMultiChoiceItems(choices, checkedChoices) { dialog, which, isChecked ->
                    checkedItem = which
                    callback.invoke(which)
                    dialog.dismiss()
                }
                .show()
    }

    override fun sortPostCollection(checkedItem: Int): Flowable<PagedList<EkoPost>> {
        return feedRepository
                .getCommunityFeed(community.getCommunityId())
                .sortBy(EkoCommunityFeedSortOption.values()[checkedItem])
                .build()
                .query()
    }

    override fun selectIncludeDeleted(callback: (Boolean) -> Unit) {
        MaterialDialog(this)
                .title(R.string.include_deleted)
                .message(R.string.include_deleted_message)
                .show {
                    positiveButton(text = "YES") {
                        callback.invoke(true)
                    }
                    negativeButton(text = "NO") {
                        callback.invoke(false)
                    }
                }
    }

    override fun getPostCollectionByIncludeDeleted(isIncludeDeleted: Boolean): Flowable<PagedList<EkoPost>> {
        return feedRepository
                .getCommunityFeed(community.getCommunityId())
                .includeDeleted(isIncludeDeleted)
                .build()
                .query()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}
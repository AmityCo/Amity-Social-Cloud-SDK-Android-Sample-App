package com.amity.sample.ascsdk.post;

import android.os.Bundle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.afollestad.materialdialogs.MaterialDialog
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.OpenUserFeedIntent
import com.amity.sample.ascsdk.intent.OpenUserPostPagingIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.amity.socialcloud.sdk.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.sdk.social.feed.AmityUserFeedSortOption
import com.ekoapp.core.utils.getCurrentClassAndMethodNames
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@Deprecated("Legacy support")
class UserFeedActivity : PostListActivity() {

    lateinit var userId: String
    private var checkedItem = 1

    override val targetType: String
        get() = "user"

    override val targetId: String
        get() = userId

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        userId = OpenUserFeedIntent.getUserId(intent)
        super.onCreate(savedInstanceState)
    }

    override fun inflateMenu(menuRes: (Int) -> Unit) {
        menuRes.invoke(R.menu.menu_post_list)
    }

    override fun usePagingDataAdapter() {
        startActivity(OpenUserPostPagingIntent(this, userId))
    }

    override fun getPostsAsPagedList(): Flowable<PagedList<AmityPost>> {
        return feedRepository
                .getUserFeed(userId)
                .sortBy(AmityUserFeedSortOption.LAST_CREATED)
                .build()
                .query()
    }

    override fun selectSortOption(callback: (Int) -> Unit) {
        val options = AmityUserFeedSortOption.values().map { Pair(it.apiKey, false) }
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

    override fun sortPostCollection(checkedItem: Int): Flowable<PagedList<AmityPost>> {
        return feedRepository
                .getUserFeed(userId)
                .sortBy(AmityUserFeedSortOption.values()[checkedItem])
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

    override fun getPostCollectionByIncludeDeleted(isIncludeDeleted: Boolean): Flowable<PagedList<AmityPost>> {
        return feedRepository
                .getUserFeed(userId)
                .includeDeleted(isIncludeDeleted)
                .build()
                .query()
    }

    override fun getPostCollectionByFeedType(feedType: AmityFeedType): Flowable<PagedList<AmityPost>> {
        TODO("Not yet implemented")
    }

    override fun checkPermission() {
        showDialog(R.string.check_permission, "", AmityPermission.EDIT_USER.value, false) { dialog, input ->
            val inputStr = input.toString()
            if (AmityPermission.values().any { it.value == inputStr }) {
                val disposable = AmityCoreClient
                        .hasPermission(AmityPermission.valueOf(inputStr))
                        .atGlobal()
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

    override fun notificationSetting() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}

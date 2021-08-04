package com.amity.sample.ascsdk.post

import android.os.Bundle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.afollestad.materialdialogs.MaterialDialog
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.OpenCommunityNotificationSettingIntent
import com.amity.sample.ascsdk.intent.OpenCommunityPostPagingIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.feed.AmityCommunityFeedSortOption
import com.amity.socialcloud.sdk.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.sdk.social.post.AmityCommunityPostQuery
import com.ekoapp.core.utils.getCurrentClassAndMethodNames
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CommunityPostPagingActivity : PostPagingActivity() {

    lateinit var community: AmityCommunity
    private var checkedItem = 0
    private val checkedPostTypes = mutableSetOf<String>()

    override val targetType: String
        get() = "community"

    override val targetId: String
        get() = community.getCommunityId()

    private val compositeDisposable = CompositeDisposable()

    private lateinit var communityPostQueryBuilder: AmityCommunityPostQuery.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        community = OpenCommunityPostPagingIntent.getCommunity(intent)
        communityPostQueryBuilder = AmitySocialClient.newPostRepository()
            .getPosts()
            .targetCommunity(community.getCommunityId())
        super.onCreate(savedInstanceState)
    }

    override fun inflateMenu(menuRes: (Int) -> Unit) {
        menuRes.invoke(R.menu.menu_community_post_paging)
    }

    override fun checkPermission() {
        showDialog(
            R.string.check_permission,
            "",
            AmityPermission.EDIT_USER.value,
            false
        ) { dialog, input ->
            val inputStr = input.toString()
            if (AmityPermission.values().any { it.value == inputStr }) {
                val disposable = AmityCoreClient
                    .hasPermission(AmityPermission.valueOf(inputStr))
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

    @ExperimentalPagingApi
    override fun getPostsAsPagingData(): Flowable<PagingData<AmityPost>> {
        return communityPostQueryBuilder
            .build()
            .getPagingData()
    }

    override fun notificationSetting() {
        if (community.isJoined()) {
            startActivity(OpenCommunityNotificationSettingIntent(this, community.getCommunityId()))
        } else {
            showToast("You're not a member of this community")
        }
    }

    override fun selectSortOption(callback: (Int) -> Unit) {
        val options = AmityCommunityFeedSortOption.values().map { Pair(it.apiKey, false) }
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

    override fun selectFilterOption(callback: (List<AmityPost.Type>) -> Unit) {
        val postTypeList = listOf(
            AmityPost.Type.IMAGE(),
            AmityPost.Type.VIDEO(),
            AmityPost.Type.FILE()
        )
        val options =
            postTypeList.map { Pair(it.getApiKey(), checkedPostTypes.contains(it.getApiKey())) }
        val choices = options.map { it.first }.toTypedArray()
        val checkedChoices = options.map { it.second }.toBooleanArray()
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.select_item)
            .setNegativeButton("Apply") { dialog, _ ->
                callback.invoke(checkedPostTypes.map { AmityPost.Type.sealedOf(it) })
                dialog.dismiss()
            }
            .setNeutralButton("All Types") { dialog, _ ->
                checkedPostTypes.clear()
                callback.invoke(listOf())
                dialog.dismiss()
            }
            .setMultiChoiceItems(choices, checkedChoices) { _, which, isChecked ->
                if (isChecked) {
                    checkedPostTypes.add(choices[which])
                } else {
                    checkedPostTypes.remove(choices[which])
                }
            }
            .show()
    }

    @ExperimentalPagingApi
    override fun sortPostCollection(checkedItem: Int): Flowable<PagingData<AmityPost>> {
        communityPostQueryBuilder.apply { this.sortBy(AmityCommunityFeedSortOption.values()[checkedItem]) }
        return communityPostQueryBuilder
            .build()
            .getPagingData()
    }

    @ExperimentalPagingApi
    override fun filterPostCollection(postTypes: List<AmityPost.Type>): Flowable<PagingData<AmityPost>> {
        communityPostQueryBuilder.apply { this.types(postTypes = postTypes) }
        return communityPostQueryBuilder
            .build()
            .getPagingData()
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

    @ExperimentalPagingApi
    override fun getPostCollectionByIncludeDeleted(isIncludeDeleted: Boolean): Flowable<PagingData<AmityPost>> {
        communityPostQueryBuilder.apply { this.includeDeleted(includeDeleted = isIncludeDeleted) }
        return communityPostQueryBuilder
            .build()
            .getPagingData()
    }

    @ExperimentalPagingApi
    override fun getPostCollectionByFeedType(feedType: AmityFeedType): Flowable<PagingData<AmityPost>> {
        communityPostQueryBuilder.apply { this.feedType(feedType = feedType) }
        return communityPostQueryBuilder
            .build()
            .getPagingData()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}
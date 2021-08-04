package com.amity.sample.ascsdk.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.social.feed.AmityPost
import io.reactivex.Flowable

class GlobalPostPagingActivity : PostPagingActivity() {

    override val targetType: String
        get() = "user"

    override val targetId: String
        get() = AmityCoreClient.getUserId()

    var globalPostQueryBuilder = AmitySocialClient.newFeedRepository()
        .getGlobalFeed()


    @ExperimentalPagingApi
    override fun getPostsAsPagingData(): Flowable<PagingData<AmityPost>> {
        return globalPostQueryBuilder
            .build()
            .getPagingData()
    }

    override fun selectSortOption(callback: (Int) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun selectFilterOption(callback: (List<AmityPost.Type>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun sortPostCollection(checkedItem: Int): Flowable<PagingData<AmityPost>> {
        TODO("Not yet implemented")
    }

    override fun filterPostCollection(postTypes: List<AmityPost.Type>): Flowable<PagingData<AmityPost>> {
        TODO("Not yet implemented")
    }

    override fun selectIncludeDeleted(callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getPostCollectionByIncludeDeleted(isIncludeDeleted: Boolean): Flowable<PagingData<AmityPost>> {
        TODO("Not yet implemented")
    }

    override fun getPostCollectionByFeedType(feedType: AmityFeedType): Flowable<PagingData<AmityPost>> {
        TODO("Not yet implemented")
    }

    override fun checkPermission() {
        TODO("Not yet implemented")
    }

    override fun notificationSetting() {
        TODO("Not yet implemented")
    }

    override fun inflateMenu(menuRes: (Int) -> Unit) {
        menuRes.invoke(R.menu.menu_global_post_list)
    }
}
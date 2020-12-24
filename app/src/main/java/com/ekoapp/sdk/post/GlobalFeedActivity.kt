package com.ekoapp.sdk.post

import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.feed.EkoPost
import com.ekoapp.sdk.R
import io.reactivex.Flowable

class GlobalFeedActivity : PostListActivity() {

    override val targetType: String
        get() = "user"

    override val targetId: String
        get() = EkoClient.getUserId()

    override fun getPostCollection(): Flowable<PagedList<EkoPost>> {
        return feedRepository.getGlobalFeed()
                .build()
                .query()
    }

    override fun selectSortOption(callback: (Int) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun sortPostCollection(checkedItem: Int): Flowable<PagedList<EkoPost>> {
        TODO("Not yet implemented")
    }

    override fun selectIncludeDeleted(callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getPostCollectionByIncludeDeleted(isIncludeDeleted: Boolean): Flowable<PagedList<EkoPost>> {
        TODO("Not yet implemented")
    }

    override fun checkPermission() {
        TODO("Not yet implemented")
    }

    override fun inflateMenu(menuRes: (Int) -> Unit) {
        menuRes.invoke(R.menu.menu_global_post_list)
    }
}
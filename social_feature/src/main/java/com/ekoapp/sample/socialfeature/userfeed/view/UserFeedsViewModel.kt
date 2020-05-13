package com.ekoapp.sample.socialfeature.userfeed.view

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ekoapp.ekosdk.*
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import javax.inject.Inject

class UserFeedsViewModel @Inject constructor() : DisposableViewModel() {

    val editFeedsActionRelay = SingleLiveData<EditUserFeedsData>()

    fun observeEditFeedsPage(): SingleLiveData<EditUserFeedsData> = editFeedsActionRelay

    fun getUserFeeds(): LiveData<PagedList<EkoPost>> {
        return EkoClient.newFeedRepository().getUserFeed(EkoClient.getUserId(), EkoUserFeedSortOption.LAST_CREATED)
    }

    fun getUserList(): LiveData<PagedList<EkoUser>> {
        return EkoClient.newUserRepository().getAllUsers(EkoUserSortOption.DISPLAYNAME)
    }

    fun deletePost(item: EkoPost) {
        EkoClient.newFeedRepository().editPost(item.postId)
                .delete()
                .subscribe()
    }
}
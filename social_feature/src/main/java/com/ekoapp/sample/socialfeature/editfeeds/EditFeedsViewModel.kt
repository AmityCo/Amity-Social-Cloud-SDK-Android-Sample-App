package com.ekoapp.sample.socialfeature.editfeeds

import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import com.ekoapp.sample.socialfeature.repositories.FeedRepository
import javax.inject.Inject

class EditFeedsViewModel @Inject constructor(private val feedRepository: FeedRepository) : DisposableViewModel() {
    private var editUserFeedsData: EditUserFeedsData? = null

    fun getIntentUserData(actionRelay: (EditUserFeedsData) -> Unit) {
        editUserFeedsData?.let(actionRelay::invoke)
    }

    fun bindEditPost(postId: String, description: String) {
        feedRepository.editPost(postId, description).subscribe()
    }

    fun setupIntent(data: EditUserFeedsData?) {
        editUserFeedsData = data
    }

}
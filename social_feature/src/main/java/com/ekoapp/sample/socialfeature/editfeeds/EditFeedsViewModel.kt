package com.ekoapp.sample.socialfeature.editfeeds

import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import javax.inject.Inject

class EditFeedsViewModel @Inject constructor() : DisposableViewModel() {
    private var editUserFeedsData: EditUserFeedsData? = null

    fun getIntentUserData(actionRelay: (EditUserFeedsData) -> Unit) {
        editUserFeedsData?.let(actionRelay::invoke)
    }

    fun editPost(postId: String, description: String) {
        EkoClient.newFeedRepository().editPost(postId)
                .text(description)
                .apply()
                .subscribe()
    }

    fun setupIntent(data: EditUserFeedsData?) {
        editUserFeedsData = data
    }

}
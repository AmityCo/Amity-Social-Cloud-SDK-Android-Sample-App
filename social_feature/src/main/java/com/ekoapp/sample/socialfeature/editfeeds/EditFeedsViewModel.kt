package com.ekoapp.sample.socialfeature.editfeeds

import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import javax.inject.Inject

class EditFeedsViewModel @Inject constructor() : DisposableViewModel() {

    fun editPost(postId: String, description: String) {
        EkoClient.newFeedRepository().editPost(postId)
                .text(description)
                .apply()
                .subscribe()
    }

}
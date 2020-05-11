package com.ekoapp.sample.socialfeature.userfeed.view.createfeeds

import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import javax.inject.Inject

class CreateFeedsViewModel @Inject constructor() : DisposableViewModel() {

    fun createPost(description: String) {
        EkoClient.newFeedRepository().createPost()
                .targetUser(EkoClient.getUserId())
                .text(description)
                .build()
                .post()
                .subscribe()
    }
}
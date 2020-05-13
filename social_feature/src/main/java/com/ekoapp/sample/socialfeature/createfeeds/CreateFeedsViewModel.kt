package com.ekoapp.sample.socialfeature.createfeeds

import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.users.data.UserData
import javax.inject.Inject

class CreateFeedsViewModel @Inject constructor() : DisposableViewModel() {

    private var userDataIntent: UserData? = null

    fun getIntentUserData(actionRelay: (UserData) -> Unit) {
        userDataIntent?.let(actionRelay::invoke)
    }

    fun createPost(userId: String, description: String) {
        EkoClient.newFeedRepository().createPost()
                .targetUser(userId)
                .text(description)
                .build()
                .post()
                .subscribe()
    }

    fun setupIntent(data: UserData?) {
        userDataIntent = data ?: UserData(userId = EkoClient.getUserId())
    }
}
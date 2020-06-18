package com.ekoapp.sample.socialfeature.createfeeds

import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.repositories.FeedRepository
import com.ekoapp.sample.socialfeature.users.data.UserData
import javax.inject.Inject

class CreateFeedsViewModel @Inject constructor(private val feedRepository: FeedRepository) : DisposableViewModel() {

    private var userDataIntent: UserData? = null

    fun getIntentUserData(actionRelay: (UserData) -> Unit) {
        userDataIntent?.let(actionRelay::invoke)
    }

    fun bindCreatePost(userId: String, description: String) {
        feedRepository.createPost(userId, description).subscribe()
    }

    fun setupIntent(data: UserData?) {
        userDataIntent = data ?: UserData(userId = EkoClient.getUserId())
    }
}
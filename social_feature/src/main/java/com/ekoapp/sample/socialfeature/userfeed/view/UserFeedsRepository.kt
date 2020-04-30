package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import com.ekoapp.sample.socialfeature.userfeed.model.SampleUserFeedsResponse
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

data class DeleteUserFeedsResult(val id: String, val isDeleted: Boolean)

class UserFeedsRepository @Inject constructor() {

    fun getUserFeedsFromGson(context: Context): SampleUserFeedsResponse {
        val data = "sample_user_feeds.json"
        val moviesString = context.assets.open(data).bufferedReader().use { it.readText() }
        return Gson().fromJson(moviesString, SampleUserFeedsResponse::class.java)
    }

    fun sendDeleteFeeds(id: String): Single<DeleteUserFeedsResult> {
        return Single.just(DeleteUserFeedsResult(id, true))
    }
}
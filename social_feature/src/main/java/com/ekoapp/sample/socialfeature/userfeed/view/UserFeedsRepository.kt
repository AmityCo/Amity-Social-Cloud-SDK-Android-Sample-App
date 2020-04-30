package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.model.SampleUserFeedsResponse
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

sealed class UserFeedsTypeSealed {
    class CreateFeeds(val result: SampleFeedsResponse) : UserFeedsTypeSealed()
    class EditFeeds(val result: SampleFeedsResponse) : UserFeedsTypeSealed()
    class DeleteFeeds(val result: DeleteUserFeedsResult) : UserFeedsTypeSealed()
    class ErrorResult(val t: Throwable) : UserFeedsTypeSealed()
}

data class DeleteUserFeedsResult(val id: String, val isDeleted: Boolean)

class UserFeedsRepository @Inject constructor() {

    fun getUserFeedsFromGson(context: Context): SampleUserFeedsResponse {
        val data = "sample_user_feeds.json"
        val moviesString = context.assets.open(data).bufferedReader().use { it.readText() }
        return Gson().fromJson(moviesString, SampleUserFeedsResponse::class.java)
    }

    fun sendDeleteFeeds(id: String): Single<UserFeedsTypeSealed> {
        val result = DeleteUserFeedsResult(id, true)
        return Single.just(UserFeedsTypeSealed.DeleteFeeds(result))
    }
}
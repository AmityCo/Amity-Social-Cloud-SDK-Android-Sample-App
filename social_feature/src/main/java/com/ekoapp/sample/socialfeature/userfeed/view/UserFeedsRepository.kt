package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.model.SampleUserFeedsResponse
import com.google.gson.Gson
import javax.inject.Inject

class UserFeedsRepository @Inject constructor() {

    val isDeletedLive = MutableLiveData<SampleFeedsResponse>()

    fun getSampleDataFeeds(context: Context): SampleUserFeedsResponse {
        val data = "sample_user_feeds.json"
        val moviesString = context.assets.open(data).bufferedReader().use { it.readText() }
        return Gson().fromJson(moviesString, SampleUserFeedsResponse::class.java)
    }

    fun sendDeleteFeeds(isDeleted: Boolean) {
        isDeletedLive.postValue(SampleFeedsResponse(
                id = "4538654937309",
                creator = "User1",
                avatar = "",
                lastCreated = "",
                description = "Post1",
                isLiked = false,
                isDeleted = isDeleted
        ))
    }

}
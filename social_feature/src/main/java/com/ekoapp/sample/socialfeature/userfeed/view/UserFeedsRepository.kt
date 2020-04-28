package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import com.ekoapp.sample.socialfeature.userfeed.model.SampleUserFeedsResponse
import com.google.gson.Gson
import javax.inject.Inject

class UserFeedsRepository @Inject constructor() {

    fun getSampleDataFeeds(context: Context): SampleUserFeedsResponse {
        val data = "sample_user_feeds.json"
        val moviesString = context.assets.open(data).bufferedReader().use { it.readText() }
        return Gson().fromJson(moviesString, SampleUserFeedsResponse::class.java)
    }
}
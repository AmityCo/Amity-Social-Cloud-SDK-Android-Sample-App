package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Context
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import javax.inject.Inject

class CreateFeedsViewModel @Inject constructor(private val context: Context) : DisposableViewModel() {

    fun mockCreateFeeds(description: String): SampleFeedsResponse = SampleFeedsResponse(
            id = "4538654937309",
            creator = "User1",
            avatar = "",
            lastCreated = "",
            description = description,
            isLiked = false,
            isDeleted = false
    )
}
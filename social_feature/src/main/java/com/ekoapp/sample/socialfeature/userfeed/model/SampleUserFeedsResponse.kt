package com.ekoapp.sample.socialfeature.userfeed.model

data class SampleUserFeedsResponse(
        val userId: String,
        val feeds: List<SampleFeedsResponse>
)
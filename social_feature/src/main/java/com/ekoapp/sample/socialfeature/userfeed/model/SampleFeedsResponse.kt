package com.ekoapp.sample.socialfeature.userfeed.model

data class SampleFeedsResponse(
        val id: String,
        val creator: String,
        val avatar: String,
        val lastCreated: String,
        val description: String,
        val isLiked: Boolean
)

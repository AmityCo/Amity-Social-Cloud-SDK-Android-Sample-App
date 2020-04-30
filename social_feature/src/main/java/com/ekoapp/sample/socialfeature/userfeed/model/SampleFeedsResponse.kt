package com.ekoapp.sample.socialfeature.userfeed.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SampleFeedsResponse(
        val id: String,
        val creator: String,
        val avatar: String,
        val lastCreated: String,
        val description: String,
        val isLiked: Boolean,
        val isDeleted: Boolean
) : Parcelable

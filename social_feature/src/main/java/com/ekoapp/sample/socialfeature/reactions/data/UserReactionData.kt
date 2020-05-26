package com.ekoapp.sample.socialfeature.reactions.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserReactionData(val postId: String) : Parcelable
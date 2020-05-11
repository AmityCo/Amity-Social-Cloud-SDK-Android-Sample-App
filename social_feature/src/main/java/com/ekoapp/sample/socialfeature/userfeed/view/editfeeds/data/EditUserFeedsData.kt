package com.ekoapp.sample.socialfeature.userfeed.view.editfeeds.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditUserFeedsData(val postId: String, val description: String) : Parcelable
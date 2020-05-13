package com.ekoapp.sample.socialfeature.editfeeds.data

import android.os.Parcelable
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditUserFeedsData(val userData: UserData, val postId: String, val description: String) : Parcelable
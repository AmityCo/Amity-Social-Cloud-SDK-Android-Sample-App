package com.amity.sdk_versioning

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Environment(
    var apiKey: String,
    var httpUrl: String,
    var socketUrl: String
) : Parcelable
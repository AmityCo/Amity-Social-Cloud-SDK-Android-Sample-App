package com.ekoapp.sample.chatfeature.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationData(val channelId: String, val isAllowed: Boolean) : Parcelable
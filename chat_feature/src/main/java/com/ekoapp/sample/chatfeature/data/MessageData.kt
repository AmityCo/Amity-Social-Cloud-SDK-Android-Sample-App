package com.ekoapp.sample.chatfeature.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageData(val channelId: String,
                       val includingTags: String,
                       val excludingTags: String,
                       val stackFromEnd: Boolean) : Parcelable
package com.ekoapp.sample.chatfeature.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChannelData(val channelId: String, val parentId: String? = null) : Parcelable
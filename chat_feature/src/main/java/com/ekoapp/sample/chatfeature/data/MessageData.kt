package com.ekoapp.sample.chatfeature.data

import android.os.Parcelable
import com.ekoapp.ekosdk.EkoTags
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageData(val channelId: String,
                       val parentId: String? = null,
                       val includingTags: EkoTags,
                       val excludingTags: EkoTags,
                       val stackFromEnd: Boolean) : Parcelable
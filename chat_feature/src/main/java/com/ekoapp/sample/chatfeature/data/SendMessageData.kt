package com.ekoapp.sample.chatfeature.data

import android.net.Uri
import android.os.Parcelable
import com.ekoapp.sample.core.data.Metadata
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendMessageData(val channelId: String,
                           val parentId: String? = null,
                           val messageId: String? = null,
                           val text: String? = null,
                           val image: Uri? = null,
                           val file: Uri? = null,
                           val custom: List<Metadata>? = null) : Parcelable
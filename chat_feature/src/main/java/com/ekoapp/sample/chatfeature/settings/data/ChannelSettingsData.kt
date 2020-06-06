package com.ekoapp.sample.chatfeature.settings.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChannelSettingsData(val types: Set<String>,
                               val filter: String,
                               val includingTags: Set<String>,
                               val excludingTags: Set<String>) : Parcelable
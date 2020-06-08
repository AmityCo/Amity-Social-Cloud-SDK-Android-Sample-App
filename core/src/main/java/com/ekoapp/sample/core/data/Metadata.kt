package com.ekoapp.sample.core.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Metadata(val key: String, val value: String) : Parcelable
package com.ekoapp.sample.chatfeature.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReactionData(val name: String,
                        val icon: Int) : Parcelable
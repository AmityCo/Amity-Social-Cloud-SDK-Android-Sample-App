package com.ekoapp.sample.socialfeature.userfeed.model

import android.os.Parcelable
import com.ekoapp.ekosdk.EkoPost
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class WrapEkoUserFeedsResponse(
        val data: @RawValue EkoPost
) : Parcelable

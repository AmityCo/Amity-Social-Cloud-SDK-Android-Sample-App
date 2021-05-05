package com.ekoapp.sdk_versioning

import android.content.Context
import com.amity.socialcloud.sdk.AmityRegionalEndpoint

object SampleUrl {

    fun getHttpUrl(context: Context): String {
        return AmityRegionalEndpoint.SG
    }

    fun getSocketUrl(context: Context): String {
        return AmityRegionalEndpoint.SG
    }

}
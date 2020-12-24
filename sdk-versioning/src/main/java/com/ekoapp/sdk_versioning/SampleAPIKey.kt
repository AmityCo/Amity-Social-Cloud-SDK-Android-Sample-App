package com.ekoapp.sdk_versioning

import android.content.Context


object SampleAPIKey {

    fun get(context: Context): String {
        return context.getString(R.string.sdk_api_key)
    }

}
package com.ekoapp.sample.register

import android.content.Context
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sample.core.preferences.PreferenceHelper
import com.ekoapp.sample.core.preferences.PreferenceHelper.displayName
import com.ekoapp.sample.core.preferences.PreferenceHelper.registerDevice
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterRepository @Inject constructor(val context: Context) {

    private val defaultPrefs = PreferenceHelper.defaultPreference(context)

    fun registerEkoClient(displayName: String): Completable {
        defaultPrefs.displayName = displayName
        return EkoClient.registerDevice(EkoClient.getUserId(), displayName)
                .subscribeOn(Schedulers.io())
                .doOnComplete {
                    defaultPrefs.registerDevice = true
                }
                .doOnError {
                    defaultPrefs.registerDevice = false
                }
    }
}
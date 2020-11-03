package com.ekoapp.simplechat

import androidx.multidex.MultiDexApplication
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.push.EkoBaidu

class SampleApp : MultiDexApplication() {

    companion object {
        private lateinit var APP: SampleApp

        fun get(): SampleApp {
            return APP
        }
    }

    override fun onCreate() {
        super.onCreate()
        APP = this

        EkoClient.setup(SimplePreferences.getApiKey().get())
                .andThen(EkoBaidu.create(this).setup("BZ2CnTh6qphSUl66c16Xk7AG"))
                .subscribe()
    }
}
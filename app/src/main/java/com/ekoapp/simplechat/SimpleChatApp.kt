package com.ekoapp.simplechat

import androidx.multidex.MultiDexApplication
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.initDi
import com.ekoapp.push.EkoBaidu
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SimpleChatApp : MultiDexApplication(), HasAndroidInjector {

    companion object {
        private lateinit var APP: SimpleChatApp
        fun get(): SimpleChatApp {
            return APP
        }
    }

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = injector
    override fun onCreate() {
        super.onCreate()
        APP = this
        EkoClient.setup(SimplePreferences.getApiKey().get())
                .andThen(EkoBaidu.create(this).setup("BZ2CnTh6qphSUl66c16Xk7AG"))
                .subscribe()
        initDi(this)
    }
}
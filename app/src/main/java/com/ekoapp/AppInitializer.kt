package com.ekoapp


import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.push.EkoBaidu
import com.ekoapp.sample.core.constant.EKO_BAIDU_KEY
import com.ekoapp.sample.core.di.ContextModule
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.DaggerCoreComponent
import com.ekoapp.sample.utils.PreferenceHelper.defaultPreference
import com.ekoapp.sample.utils.PreferenceHelper.ekoApiKey

fun initEkoClient(app: App) {
    EkoClient.setup(defaultPreference(app).ekoApiKey.toString())
            .andThen(EkoBaidu.create(app).setup(EKO_BAIDU_KEY))
            .subscribe()
}

fun initCoreDi(app: App): CoreComponent =
        DaggerCoreComponent
                .builder()
                .contextModule(ContextModule(app))
                .build()

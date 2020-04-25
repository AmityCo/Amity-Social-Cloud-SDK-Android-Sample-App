package com.ekoapp.sample.app

import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.push.EkoBaidu
import com.ekoapp.sample.SimplePreferences
import com.ekoapp.sample.core.di.ContextModule
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.DaggerCoreComponent


fun initEkoClient(app: App) {
    EkoClient.setup(SimplePreferences.getApiKey().get())
            .andThen(EkoBaidu.create(app).setup("BZ2CnTh6qphSUl66c16Xk7AG"))
            .subscribe()
}

fun initCoreDi(app: App): CoreComponent =
        DaggerCoreComponent
                .builder()
                .contextModule(ContextModule(app))
                .build()
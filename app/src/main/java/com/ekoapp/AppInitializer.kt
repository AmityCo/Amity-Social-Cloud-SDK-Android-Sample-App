package com.ekoapp


import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.push.EkoBaidu
import com.ekoapp.sample.core.di.ContextModule
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.DaggerCoreComponent
import com.ekoapp.simplechat.SimplePreferences


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

package com.ekoapp

import com.ekoapp.di.AppModule
import com.ekoapp.di.DaggerCoreComponent
import com.ekoapp.simplechat.SimpleChatApp


fun initDi(app: SimpleChatApp) {
    DaggerCoreComponent.builder()
            .appModule(AppModule(app))
            .build()
            .inject(app)
}

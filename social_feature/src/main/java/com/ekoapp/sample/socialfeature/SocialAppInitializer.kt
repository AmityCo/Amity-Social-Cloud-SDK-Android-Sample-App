package com.ekoapp.sample.socialfeature

import android.os.Looper
import com.ekoapp.sample.socialfeature.di.SocialAppModule
import com.ekoapp.sample.socialfeature.di.DaggerSocialComponent
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins

fun initDi(app: SocialApplication) {
    DaggerSocialComponent.builder()
            .socialAppModule(SocialAppModule(app))
            .build()
            .inject(app)
}

fun initRx() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler {
        AndroidSchedulers.from(Looper.getMainLooper(), true)
    }
    RxJavaPlugins.setErrorHandler {
        (it as? UndeliverableException)?.printStackTrace()
    }
}

package com.ekoapp.sample.core.app

import androidx.multidex.MultiDexApplication
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.CoreComponentProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : MultiDexApplication(), HasAndroidInjector, CoreComponentProvider {

    companion object {
        private lateinit var APP: App
        fun get(): App {
            return APP
        }
    }

    private lateinit var coreComponent: CoreComponent

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = injector
    override fun onCreate() {
        super.onCreate()
        APP = this
        coreComponent = initCoreDi(this)
        initEkoClient(this)
    }

    override fun provideCoreComponent(): CoreComponent = coreComponent
}
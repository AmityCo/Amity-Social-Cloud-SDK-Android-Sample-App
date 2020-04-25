package com.ekoapp

import androidx.multidex.MultiDexApplication
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.CoreComponentProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : MultiDexApplication(), HasAndroidInjector, CoreComponentProvider {

    private lateinit var coreComponent: CoreComponent

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onCreate() {
        super.onCreate()
        coreComponent = initCoreDi(this)
        initEkoClient(this)
    }

    override fun provideCoreComponent(): CoreComponent = coreComponent
}
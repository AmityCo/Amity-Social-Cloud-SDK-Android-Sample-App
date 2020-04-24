package com.ekoapp.di

import com.ekoapp.simplechat.SimpleChatApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
        modules = [ActivityBuilder::class,
            FragmentBuilder::class,
            AppModule::class,
            AndroidInjectionModule::class]
)
interface CoreComponent : AndroidInjector<SimpleChatApp>
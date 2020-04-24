package com.ekoapp.sample.socialfeature.di

import com.ekoapp.di.CoreComponent
import com.ekoapp.sample.socialfeature.SocialApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@SocialScope
@Component(
        dependencies = [CoreComponent::class],
        modules = [
            ActivityBuilder::class,
            FragmentBuilder::class,
            AppModule::class,
            AndroidInjectionModule::class]
)
interface SocialComponent : AndroidInjector<SocialApplication>
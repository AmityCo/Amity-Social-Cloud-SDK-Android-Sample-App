package com.ekoapp.sample.main.di

import com.ekoapp.sample.MainNavigationActivity
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.SplitInstallModule
import com.ekoapp.sample.core.di.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(
        modules = [SplitInstallModule::class],
        dependencies = [CoreComponent::class]
)
interface MainNavigationComponent {
    fun inject(activity: MainNavigationActivity)
}
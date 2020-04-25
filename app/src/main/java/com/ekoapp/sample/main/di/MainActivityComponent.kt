package com.ekoapp.sample.main.di

import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.scope.ActivityScope
import com.ekoapp.sample.main.MainActivity
import dagger.Component

@ActivityScope
@Component(
        modules = [SplitInstallModule::class],
        dependencies = [CoreComponent::class]
)
interface MainActivityComponent {
    fun inject(activity: MainActivity)
}
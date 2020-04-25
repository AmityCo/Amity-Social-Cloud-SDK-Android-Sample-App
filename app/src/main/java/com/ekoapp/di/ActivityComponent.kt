package com.ekoapp.di

import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.SplitModule
import com.ekoapp.sample.core.di.scope.ActivityScope
import com.ekoapp.view.MainNavigationActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class], modules = [SplitModule::class])
interface ActivityComponent {
    fun inject(activity: MainNavigationActivity)
}
package com.ekoapp.sample.chatfeature.di

import com.ekoapp.sample.chatfeature.settings.ChannelSettingsActivity
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.SplitInstallModule
import com.ekoapp.sample.core.di.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(
        modules = [SplitInstallModule::class],
        dependencies = [CoreComponent::class]
)
interface ChatActivityComponent {
    fun inject(activity: ChannelSettingsActivity)
}
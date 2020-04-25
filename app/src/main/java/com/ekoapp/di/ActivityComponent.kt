package com.ekoapp.di

import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.scope.ActivityScope
import com.ekoapp.simplechat.channellist.ChannelListActivity
import dagger.Component

@ActivityScope
@Component(
        modules = [SplitModule::class],
        dependencies = [CoreComponent::class]
)
interface ActivityComponent {
    fun inject(activity: ChannelListActivity)
}
package com.ekoapp.sample.chatfeature.di

import com.ekoapp.sample.chatfeature.channellist.ChannelListActivity
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.SplitModule
import com.ekoapp.sample.core.di.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class], modules = [SplitModule::class])
interface ChatActivityComponent {
    fun inject(activity: ChannelListActivity)
}
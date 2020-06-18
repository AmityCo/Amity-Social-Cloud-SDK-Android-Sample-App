package com.ekoapp.sample.chatfeature.di

import com.ekoapp.sample.chatfeature.ChatFragment
import com.ekoapp.sample.chatfeature.channels.ChannelsFragment
import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.SplitInstallModule
import com.ekoapp.sample.core.di.scope.FragmentScope
import dagger.Component

@FragmentScope
@Component(
        modules = [SplitInstallModule::class],
        dependencies = [CoreComponent::class]
)
interface ChatFragmentComponent {
    fun inject(fragment: ChatFragment)
    fun inject(fragment: ChannelsFragment)
}
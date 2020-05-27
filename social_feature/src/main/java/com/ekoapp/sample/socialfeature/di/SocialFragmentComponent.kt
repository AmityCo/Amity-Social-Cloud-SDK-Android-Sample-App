package com.ekoapp.sample.socialfeature.di

import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.SplitInstallModule
import com.ekoapp.sample.core.di.scope.FragmentScope
import com.ekoapp.sample.socialfeature.reactions.view.UserReactionFeedsFragment
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsFragment
import com.ekoapp.sample.socialfeature.users.view.UsersFragment
import dagger.Component

@FragmentScope
@Component(
        modules = [SplitInstallModule::class],
        dependencies = [CoreComponent::class]
)
interface SocialFragmentComponent {
    fun inject(fragment: UserFeedsFragment)
    fun inject(fragment: UsersFragment)
    fun inject(fragment: UserReactionFeedsFragment)
}
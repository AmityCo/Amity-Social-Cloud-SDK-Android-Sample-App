package com.ekoapp.sample.socialfeature.view.di

import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.scope.ActivityScope
import com.ekoapp.sample.socialfeature.view.MainActivity
import dagger.Component

@ActivityScope
@Component(
        dependencies = [CoreComponent::class]
)
interface SocialActivityComponent {
    fun inject(activity: MainActivity)
}
package com.ekoapp.sample.socialfeature.di

import com.ekoapp.sample.core.di.CoreComponent
import com.ekoapp.sample.core.di.SplitInstallModule
import com.ekoapp.sample.core.di.scope.ActivityScope
import com.ekoapp.sample.socialfeature.userfeed.view.createfeeds.CreateFeedsActivity
import com.ekoapp.sample.socialfeature.userfeed.view.editfeeds.EditFeedsActivity
import dagger.Component

@ActivityScope
@Component(
        modules = [SplitInstallModule::class],
        dependencies = [CoreComponent::class]
)
interface SocialActivityComponent {
    fun inject(activity: CreateFeedsActivity)
    fun inject(activity: EditFeedsActivity)
}
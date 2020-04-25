package com.ekoapp.di

import android.content.Context
import com.ekoapp.utils.split.SOCIAL_DYNAMIC_FEATURE
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class SplitModule {

    @Provides
    @Reusable
    fun splitInstallManager(context: Context): SplitInstallManager = SplitInstallManagerFactory.create(context)

    @Provides
    @Reusable
    fun splitInstallRequest(): SplitInstallRequest = SplitInstallRequest
            .newBuilder()
            .addModule(SOCIAL_DYNAMIC_FEATURE)
            .build()
}
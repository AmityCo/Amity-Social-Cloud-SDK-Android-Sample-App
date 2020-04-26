package com.ekoapp.sample.main.di

import android.content.Context
import com.ekoapp.sample.utils.splitinstall.CHAT_DYNAMIC_FEATURE
import com.ekoapp.sample.utils.splitinstall.SOCIAL_DYNAMIC_FEATURE
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Named

@Module
class SplitInstallModule {

    @Provides
    @Reusable
    fun splitInstallManager(context: Context): SplitInstallManager = SplitInstallManagerFactory.create(context)

    @Provides
    @Named("chat")
    @Reusable
    fun splitInstallChatFeatureRequest(): SplitInstallRequest = SplitInstallRequest
            .newBuilder()
            .addModule(CHAT_DYNAMIC_FEATURE)
            .build()

    @Provides
    @Named("social")
    @Reusable
    fun splitInstallSocialFeatureRequest(): SplitInstallRequest = SplitInstallRequest
            .newBuilder()
            .addModule(SOCIAL_DYNAMIC_FEATURE)
            .build()
}
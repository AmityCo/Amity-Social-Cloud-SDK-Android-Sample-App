package com.ekoapp.sample.socialfeature.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class SocialAppModule(private val app: Application) {
    @Provides
    @AppContext
    fun getApplication(): Context {
        return app
    }
}
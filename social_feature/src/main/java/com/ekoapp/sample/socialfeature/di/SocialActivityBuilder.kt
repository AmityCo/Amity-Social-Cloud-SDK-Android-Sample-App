package com.ekoapp.sample.socialfeature.di

import com.ekoapp.sample.socialfeature.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SocialActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}
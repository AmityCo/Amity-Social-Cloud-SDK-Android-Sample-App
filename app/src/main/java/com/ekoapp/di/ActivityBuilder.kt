package com.ekoapp.di

import com.ekoapp.simplechat.channellist.ChannelListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindChannelListActivity(): ChannelListActivity
}
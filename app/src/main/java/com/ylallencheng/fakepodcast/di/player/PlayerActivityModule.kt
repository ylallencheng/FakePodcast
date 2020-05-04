package com.ylallencheng.fakepodcast.di.player

import com.ylallencheng.fakepodcast.ui.player.PlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface PlayerActivityModule {

    @PlayerScope
    @ContributesAndroidInjector(
        modules = [
            PlayerModule::class
        ]
    )
    abstract fun contributePlayerActivity(): PlayerActivity
}
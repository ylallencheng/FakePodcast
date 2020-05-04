package com.ylallencheng.fakepodcast.di

import com.ylallencheng.fakepodcast.di.player.PlayerActivityModule
import com.ylallencheng.fakepodcast.di.podcast.PodcastActivityModule
import dagger.Module

@Module(
    includes = [
        PodcastActivityModule::class,
        PlayerActivityModule::class
    ]
)
class AppModule
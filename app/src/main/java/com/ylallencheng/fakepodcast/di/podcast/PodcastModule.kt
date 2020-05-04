package com.ylallencheng.fakepodcast.di.podcast

import com.ylallencheng.fakepodcast.io.db.FPDatabase
import com.ylallencheng.fakepodcast.io.network.PodcastService
import com.ylallencheng.fakepodcast.ui.podcast.PodcastRepository
import dagger.Module
import dagger.Provides

@Module
class PodcastModule {

    @PodcastScope
    @Provides
    fun providePodcastRepository(
        podcastService: PodcastService,
        fpDatabase: FPDatabase
    ) = PodcastRepository(podcastService, fpDatabase.podcastDao(), fpDatabase.collectionDao())
}
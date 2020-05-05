package com.ylallencheng.fakepodcast.di.podcast

import com.ylallencheng.fakepodcast.ui.podcast.PodcastActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface PodcastActivityModule {

    @PodcastScope
    @ContributesAndroidInjector(
        modules = [
            PodcastModule::class,
            PodcastFragmentModule::class
        ]
    )
    fun contributePodcastActivity(): PodcastActivity
}
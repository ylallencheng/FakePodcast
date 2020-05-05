package com.ylallencheng.fakepodcast.di.podcast

import com.ylallencheng.fakepodcast.ui.podcast.fragment.CollectionFragment
import com.ylallencheng.fakepodcast.ui.podcast.fragment.PodcastsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PodcastFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributePodcastsFragment(): PodcastsFragment

    @ContributesAndroidInjector
    abstract fun contributeCollectionFragment(): CollectionFragment
}
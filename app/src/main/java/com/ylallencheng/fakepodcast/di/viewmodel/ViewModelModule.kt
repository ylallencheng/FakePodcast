package com.ylallencheng.fakepodcast.di.viewmodel

import androidx.lifecycle.ViewModel
import com.ylallencheng.fakepodcast.ui.player.PlayerViewModel
import com.ylallencheng.fakepodcast.ui.podcast.PodcastViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PodcastViewModel::class)
    abstract fun bindPodcastViewModel(viewModel: PodcastViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    abstract fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel
}
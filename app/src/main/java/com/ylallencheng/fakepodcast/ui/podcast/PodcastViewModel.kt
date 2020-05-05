package com.ylallencheng.fakepodcast.ui.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ylallencheng.fakepodcast.io.model.Collection
import com.ylallencheng.fakepodcast.io.model.Podcast
import com.ylallencheng.fakepodcast.io.model.IOStatus
import javax.inject.Inject

class PodcastViewModel @Inject constructor(
    private val mPodcastRepository: PodcastRepository
) : ViewModel() {

    val podcasts: LiveData<IOStatus<List<Podcast>>> = mPodcastRepository.getPodcasts()

    val collections: LiveData<IOStatus<Collection>> = mPodcastRepository.getCollection()
}
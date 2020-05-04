package com.ylallencheng.fakepodcast.ui.podcast

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class PodcastViewModel @Inject constructor(
    private val mPodcastRepository: PodcastRepository
) : ViewModel() {
}
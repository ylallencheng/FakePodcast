package com.ylallencheng.fakepodcast.ui.podcast

import com.ylallencheng.fakepodcast.io.db.CollectionDao
import com.ylallencheng.fakepodcast.io.db.PodcastDao
import com.ylallencheng.fakepodcast.io.network.PodcastService
import javax.inject.Inject

class PodcastRepository @Inject constructor(
    private val mPodcastService: PodcastService,
    private val mPodcastDao: PodcastDao,
    private val mCollectionDao: CollectionDao
) {
    
}
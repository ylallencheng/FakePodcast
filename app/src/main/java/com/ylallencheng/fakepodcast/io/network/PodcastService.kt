package com.ylallencheng.fakepodcast.io.network

import com.ylallencheng.fakepodcast.io.model.CollectionData
import com.ylallencheng.fakepodcast.io.model.Podcasts
import com.ylallencheng.fakepodcast.io.model.Response
import retrofit2.http.GET

interface PodcastService {

    @GET("getcasts")
    suspend fun getPodcasts(): Response<Podcasts>

    @GET("getcastdetail")
    suspend fun getCollection(): Response<CollectionData>
}
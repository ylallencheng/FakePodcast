package com.ylallencheng.fakepodcast.ui.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ylallencheng.fakepodcast.io.db.CollectionDao
import com.ylallencheng.fakepodcast.io.db.PodcastDao
import com.ylallencheng.fakepodcast.io.model.Collection
import com.ylallencheng.fakepodcast.io.model.Podcast
import com.ylallencheng.fakepodcast.io.model.IOStatus
import com.ylallencheng.fakepodcast.io.network.PodcastService
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import javax.inject.Inject

class PodcastRepository @Inject constructor(
    private val mPodcastService: PodcastService,
    private val mPodcastDao: PodcastDao,
    private val mCollectionDao: CollectionDao
) {

    fun getPodcasts(): LiveData<IOStatus<List<Podcast>>> =
        liveData(Dispatchers.IO) {

            emit(IOStatus.Loading())

            val podcasts = mPodcastDao.getAllPodcasts()
            if (podcasts.isNotEmpty()) {
                emit(IOStatus.Successful(data = podcasts))
            }

            try {
                val response = mPodcastService.getPodcasts()
                val responsePodcasts = response.data?.podcasts

                if (responsePodcasts != null) {
                    mPodcastDao.insertPodcasts(responsePodcasts)
                    emit(IOStatus.Successful(data = responsePodcasts))
                } else {
                    emit(IOStatus.Failed<List<Podcast>>(errorMessage = "Result is empty"))
                }

            } catch (e: HttpException) {
                if (podcasts.isNotEmpty()) {
                    emit(IOStatus.Successful(data = podcasts))
                } else {
                    emit(IOStatus.Failed<List<Podcast>>(errorMessage = "Something went wrong"))
                }
            }
        }

    fun getCollection(): LiveData<IOStatus<Collection>> =
        liveData(Dispatchers.IO) {
            emit(IOStatus.Loading())

            val collection = mCollectionDao.getCollection(160904630)
            emit(IOStatus.Successful(data = collection))

            try {
                val response = mPodcastService.getCollection()
                val responseCollection = response.data?.collection

                if (responseCollection != null) {
                    mCollectionDao.insertCollections(listOf(responseCollection))
                    emit(IOStatus.Successful(data = responseCollection))
                } else {
                    emit(IOStatus.Failed<Collection>(errorMessage = "collection is empty"))
                }

            } catch (e: HttpException) {
                emit(IOStatus.Successful(data = collection))
            }
        }
}
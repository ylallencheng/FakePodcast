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
            if (podcasts?.isNotEmpty() == true) {
                emit(IOStatus.Successful(data = podcasts))
            } else {
                try {
                    val response = mPodcastService.getPodcasts()
                    val responsePodcasts = response.data?.podcasts

                    if (responsePodcasts != null) {
                        mPodcastDao.insertPodcasts(responsePodcasts)
                        emit(IOStatus.Successful(data = responsePodcasts))
                    } else {
                        emit(IOStatus.Failed<List<Podcast>>(errorMessage = "podcast list is empty"))
                    }

                } catch (e: HttpException) {
                    emit(IOStatus.Failed<List<Podcast>>(errorMessage = "Something went wrong"))
                }
            }
        }

    fun getCollection(): LiveData<IOStatus<Collection>> =
        liveData(Dispatchers.IO) {
            emit(IOStatus.Loading())
            val collection = mCollectionDao.getCollection(160904630)
            if (collection != null) {
                emit(IOStatus.Successful(data = collection))
            } else {
                try {
                    val response = mPodcastService.getCollection()
                    val responseCollection = response.data?.collection

                    if (responseCollection != null) {
                        mCollectionDao.insertCollection(responseCollection)
                        emit(IOStatus.Successful(data = responseCollection))
                    } else {
                        emit(IOStatus.Failed<Collection>(errorMessage = "collection is null"))
                    }

                } catch (e: HttpException) {
                    emit(IOStatus.Failed<Collection>(errorMessage = "Something went wrong"))
                }
            }
        }
}
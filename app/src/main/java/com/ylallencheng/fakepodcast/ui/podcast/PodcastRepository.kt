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

/**
 * The Model source of PodcastActivity
 */
class PodcastRepository @Inject constructor(
    private val mPodcastService: PodcastService,
    private val mPodcastDao: PodcastDao,
    private val mCollectionDao: CollectionDao
) {

    /**
     * Get podcasts
     */
    fun getPodcasts(): LiveData<IOStatus<List<Podcast>>> =
        liveData(Dispatchers.IO) {
            // start loading
            emit(IOStatus.Loading())
            // get data from db
            val podcasts = mPodcastDao.getAllPodcasts()

            // check if data from db is valid
            if (podcasts?.isNotEmpty() == true) {
                // data from db is valid, callback with data
                emit(IOStatus.Successful(data = podcasts))
            } else {
                // data from db is invalid, try fetch data from network
                try {
                    // get data from network
                    val response = mPodcastService.getPodcasts()
                    // get actual data from response
                    val responsePodcasts = response.data?.podcasts
                    // validate the data from response
                    if (responsePodcasts != null) {
                        // data from response is valid, update db and callback with data
                        mPodcastDao.insertPodcasts(responsePodcasts)
                        emit(IOStatus.Successful(data = responsePodcasts))
                    } else {
                        // data from response is invalid, callback with error message
                        emit(IOStatus.Failed<List<Podcast>>(errorMessage = "podcast list is empty"))
                    }

                } catch (e: HttpException) {
                    // fetching from network failed, callback with error message
                    emit(IOStatus.Failed<List<Podcast>>(errorMessage = "Something went wrong"))
                }
            }
        }

    /**
     * Get collection
     */
    fun getCollection(): LiveData<IOStatus<Collection>> =
        liveData(Dispatchers.IO) {
            // start loading
            emit(IOStatus.Loading())
            // get data from db
            val collection = mCollectionDao.getCollection(160904630)
            // check if data from db is valid
            if (collection != null) {
                // data from db is valid, callback with data
                emit(IOStatus.Successful(data = collection))
            } else {
                // data from db is invalid, try fetch data from network
                try {
                    // get data from network
                    val response = mPodcastService.getCollection()
                    // get actual data from response
                    val responseCollection = response.data?.collection
                    // validate the data from response
                    if (responseCollection != null) {
                        // data from response is valid, update db and callback with data
                        mCollectionDao.insertCollection(responseCollection)
                        emit(IOStatus.Successful(data = responseCollection))
                    } else {
                        // data from response is invalid, callback with error message
                        emit(IOStatus.Failed<Collection>(errorMessage = "collection is null"))
                    }

                } catch (e: HttpException) {
                    // fetching from network failed, callback with error message
                    emit(IOStatus.Failed<Collection>(errorMessage = "Something went wrong"))
                }
            }
        }
}
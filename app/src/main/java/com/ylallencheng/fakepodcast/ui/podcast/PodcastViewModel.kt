package com.ylallencheng.fakepodcast.ui.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.ylallencheng.fakepodcast.io.model.Collection
import com.ylallencheng.fakepodcast.io.model.Podcast
import com.ylallencheng.fakepodcast.io.model.IOStatus
import com.ylallencheng.fakepodcast.ui.podcast.bindingmodel.PodcastBindingModel
import com.ylallencheng.fakepodcast.util.SingleLiveEvent
import javax.inject.Inject

class PodcastViewModel @Inject constructor(
    private val mPodcastRepository: PodcastRepository
) : ViewModel() {

    val getPodcasts: LiveData<IOStatus<List<Podcast>>> = mPodcastRepository.getPodcasts()

    val getCollection: LiveData<IOStatus<Collection>> = mPodcastRepository.getCollection()

    val podcastBindingModels: MutableLiveData<List<PodcastBindingModel>> = MutableLiveData()

    fun convertPodcastToBindingModel() {
        podcastBindingModels.value =
            getPodcasts.value?.data
                ?.map {
                    PodcastBindingModel(
                        artworkUrl = it.artworkImageUrl ?: "",
                        artistName = it.artistName ?: "",
                        podcastName = it.name ?: ""
                    )
                } ?: listOf()
    }

    val navigateToCollectionTrigger: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun podcastSelected() {
        // navigate to collection
        navigateToCollectionTrigger.trigger()
    }
}
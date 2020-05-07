package com.ylallencheng.fakepodcast.ui.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ylallencheng.fakepodcast.io.model.Collection
import com.ylallencheng.fakepodcast.io.model.Podcast
import com.ylallencheng.fakepodcast.io.model.IOStatus
import com.ylallencheng.fakepodcast.ui.podcast.fragment.collectionfeed.CollectionFeedBindingModel
import com.ylallencheng.fakepodcast.ui.podcast.fragment.podcasts.PodcastBindingModel
import com.ylallencheng.fakepodcast.util.SingleLiveEvent
import javax.inject.Inject

class PodcastViewModel @Inject constructor(
    podcastRepository: PodcastRepository
) : ViewModel() {

    val getPodcasts: LiveData<IOStatus<List<Podcast>>> = podcastRepository.getPodcasts()

    val getCollection: LiveData<IOStatus<Collection>> = podcastRepository.getCollection()

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

    val collectionBindingModels: MutableLiveData<List<CollectionFeedBindingModel>> =
        MutableLiveData()

    fun convertCollectionToBindingModel() {
        collectionBindingModels.value =
            getCollection.value?.data?.contentFeed
                ?.map {
                    CollectionFeedBindingModel(
                        feedName = it.title ?: "",
                        contentUrl = it.contentUrl ?: "",
                        description = it.description ?: ""
                    )
                } ?: listOf()
    }

    val navigateToCollectionTrigger: SingleLiveEvent<PodcastBindingModel> = SingleLiveEvent()

    fun podcastSelected(bindingModel: PodcastBindingModel) {
        // navigate to collection
        navigateToCollectionTrigger.value = bindingModel
    }

    val navigateToPlayerTrigger: SingleLiveEvent<CollectionFeedBindingModel> = SingleLiveEvent()

    fun collectionFeedSelected(bindingModel: CollectionFeedBindingModel) {
        // navigate to player
        navigateToPlayerTrigger.value = bindingModel
    }
}
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

/**
 * The ViewModel of PodcastActivity
 */
class PodcastViewModel @Inject constructor(
    podcastRepository: PodcastRepository
) : ViewModel() {

    /* ------------------------------ Podcasts */

    /*
        Podcast data source from repository
     */
    val getPodcasts: LiveData<IOStatus<List<Podcast>>> = podcastRepository.getPodcasts()

    /*
        View binding models for displaying in list item
     */
    val podcastBindingModels: MutableLiveData<List<PodcastBindingModel>> = MutableLiveData()

    /**
     * Converting data podcast to view binding model
     */
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

    /*
        Trigger of navigating to collection page when a podcast is selected
     */
    val navigateToCollectionTrigger: SingleLiveEvent<PodcastBindingModel> = SingleLiveEvent()

    /**
     * A podcast has been selected
     */
    fun podcastSelected(bindingModel: PodcastBindingModel) {
        // navigate to collection
        navigateToCollectionTrigger.value = bindingModel
    }

    /* ------------------------------ Collection Feeds */

    /*
        Collection data source from repository
     */
    val getCollection: LiveData<IOStatus<Collection>> = podcastRepository.getCollection()

    /*
        View binding models for displaying collection feed in list item
     */
    val collectionBindingModels: MutableLiveData<List<CollectionFeedBindingModel>> =
        MutableLiveData()

    /**
     * Convert data collection feed to view binding model
     */
    fun convertCollectionFeedToBindingModel() {
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

    /*
        Trigger of navigating to player when a collection feed is selected
     */
    val navigateToPlayerTrigger: SingleLiveEvent<CollectionFeedBindingModel> = SingleLiveEvent()

    /**
     * A collection feed has been selected
     */
    fun collectionFeedSelected(bindingModel: CollectionFeedBindingModel) {
        // navigate to player
        navigateToPlayerTrigger.value = bindingModel
    }
}
package com.ylallencheng.fakepodcast.ui.podcast.fragment.podcasts

import androidx.recyclerview.widget.DiffUtil

data class PodcastBindingModel(
    val artworkUrl: String,
    val artistName: String,
    val podcastName: String
) {

    /* ------------------------------ Companion Object */

    companion object {

        /*
            The item callback for list adapter to check item differences
         */
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PodcastBindingModel>() {
            override fun areItemsTheSame(
                oldItem: PodcastBindingModel,
                newItem: PodcastBindingModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PodcastBindingModel,
                newItem: PodcastBindingModel
            ): Boolean = oldItem == newItem
        }
    }
}
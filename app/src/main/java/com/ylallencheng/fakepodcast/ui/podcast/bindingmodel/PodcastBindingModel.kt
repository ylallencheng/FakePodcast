package com.ylallencheng.fakepodcast.ui.podcast.bindingmodel

import androidx.recyclerview.widget.DiffUtil

class PodcastBindingModel(
    val artworkUrl: String,
    val artistName: String,
    val podcastName: String
) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PodcastBindingModel>() {
            override fun areItemsTheSame(
                oldItem: PodcastBindingModel,
                newItem: PodcastBindingModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PodcastBindingModel,
                newItem: PodcastBindingModel
            ): Boolean = oldItem.toString() == newItem.toString()
        }
    }
}
package com.ylallencheng.fakepodcast.ui.podcast.fragment.collectionfeed

import androidx.recyclerview.widget.DiffUtil

data class CollectionFeedBindingModel(
    val feedName: String,
    val contentUrl: String,
    val description: String
) {

    /* ------------------------------ Companion Object */

    companion object {

        /*
            The item callback for list adapter to check item differences
         */
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CollectionFeedBindingModel>() {
            override fun areItemsTheSame(
                oldItem: CollectionFeedBindingModel,
                newItem: CollectionFeedBindingModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: CollectionFeedBindingModel,
                newItem: CollectionFeedBindingModel
            ): Boolean = oldItem == newItem
        }
    }
}
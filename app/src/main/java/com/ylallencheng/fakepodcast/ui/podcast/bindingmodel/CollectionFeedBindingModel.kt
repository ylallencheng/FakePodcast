package com.ylallencheng.fakepodcast.ui.podcast.bindingmodel

import androidx.recyclerview.widget.DiffUtil

data class CollectionFeedBindingModel(
    val feedName: String,
    val contentUrl: String,
    val description: String
) {
    companion object {
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
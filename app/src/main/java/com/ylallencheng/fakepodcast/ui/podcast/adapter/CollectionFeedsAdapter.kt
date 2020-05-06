package com.ylallencheng.fakepodcast.ui.podcast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ylallencheng.fakepodcast.databinding.ItemCollectionFeedBinding
import com.ylallencheng.fakepodcast.ui.podcast.PodcastViewModel
import com.ylallencheng.fakepodcast.ui.podcast.bindingmodel.CollectionFeedBindingModel

class CollectionFeedsAdapter(private val mViewModel: PodcastViewModel) :
    ListAdapter<CollectionFeedBindingModel, CollectionFeedsAdapter.CollectionFeedViewHolder>(
        CollectionFeedBindingModel.DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionFeedViewHolder {
        return CollectionFeedViewHolder(
            mViewModel,
            ItemCollectionFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: CollectionFeedViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class CollectionFeedViewHolder(
        private val mViewModel: PodcastViewModel,
        private val mBinding: ItemCollectionFeedBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(bindingModel: CollectionFeedBindingModel) {
            mBinding.apply {
                mBinding.textViewFeedName.text = bindingModel.feedName
            }
        }
    }
}
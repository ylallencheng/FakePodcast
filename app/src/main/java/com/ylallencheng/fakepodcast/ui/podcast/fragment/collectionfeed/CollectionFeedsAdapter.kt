package com.ylallencheng.fakepodcast.ui.podcast.fragment.collectionfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ylallencheng.fakepodcast.databinding.ItemCollectionFeedBinding
import com.ylallencheng.fakepodcast.ui.podcast.PodcastViewModel

class CollectionFeedsAdapter(
    private val mViewModel: PodcastViewModel
) : ListAdapter<CollectionFeedBindingModel, CollectionFeedsAdapter.CollectionFeedViewHolder>(
    CollectionFeedBindingModel.DIFF_CALLBACK
) {

    /* ------------------------------ Overrides */

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

    /* ------------------------------ View Holder */

    /**
     * ViewHolder for displaying collection feed binding model
     */
    class CollectionFeedViewHolder(
        private val mViewModel: PodcastViewModel,
        private val mBinding: ItemCollectionFeedBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        /**
         * Bind view with binding model
         */
        fun bind(bindingModel: CollectionFeedBindingModel) {
            mBinding.textViewFeedName.text = bindingModel.feedName
            mBinding.root.setOnClickListener {
                mViewModel.collectionFeedSelected(bindingModel)
            }
        }
    }
}
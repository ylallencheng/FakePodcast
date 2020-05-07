package com.ylallencheng.fakepodcast.ui.podcast.fragment.podcasts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ylallencheng.fakepodcast.R
import com.ylallencheng.fakepodcast.databinding.ItemPodcastBinding
import com.ylallencheng.fakepodcast.ui.podcast.PodcastViewModel

class PodcastsAdapter(
    private val mViewModel: PodcastViewModel
) : ListAdapter<PodcastBindingModel, PodcastsAdapter.PodcastViewHolder>(
    PodcastBindingModel.DIFF_CALLBACK
) {

    /* ------------------------------ Overrides */

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PodcastViewHolder =
        PodcastViewHolder(
            mViewModel,
            ItemPodcastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: PodcastViewHolder,
        position: Int
    ) = holder.bind(getItem(position))

    /* ------------------------------ View Holder */

    /**
     * ViewHolder for displaying podcast binding model
     */
    class PodcastViewHolder(
        private val mViewModel: PodcastViewModel,
        private val mBinding: ItemPodcastBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        /**
         * Bind view with binding model
         */
        fun bind(bindingModel: PodcastBindingModel) {
            Glide
                .with(mBinding.root)
                .load(bindingModel.artworkUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(mBinding.imageViewPodcastArtwork)
            mBinding.textViewArtistName.text = bindingModel.artistName
            mBinding.textViewPodcastName.text = bindingModel.podcastName
            mBinding.root.setOnClickListener { mViewModel.podcastSelected(bindingModel) }
        }
    }
}


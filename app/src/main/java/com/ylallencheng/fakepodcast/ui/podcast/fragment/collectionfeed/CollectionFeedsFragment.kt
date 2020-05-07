package com.ylallencheng.fakepodcast.ui.podcast.fragment.collectionfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ylallencheng.fakepodcast.R
import com.ylallencheng.fakepodcast.databinding.FragmentCollectionFeedsBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
import com.ylallencheng.fakepodcast.io.model.Status
import com.ylallencheng.fakepodcast.ui.podcast.PodcastViewModel
import com.ylallencheng.fakepodcast.util.observe
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CollectionFeedsFragment : DaggerFragment() {

    // View Model
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: PodcastViewModel by activityViewModels { viewModelFactory }

    // View binding
    private val mBinding: FragmentCollectionFeedsBinding by lazy {
        FragmentCollectionFeedsBinding.inflate(layoutInflater)
    }

    // Navigation arguments
    private val mArgs: CollectionFeedsFragmentArgs by navArgs()

    /* ------------------------------ Lifecycle */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initUi()
        observe()
        return mBinding.root
    }

    /* ------------------------------ UI */

    /**
     * Initialize UI
     */
    private fun initUi() {
        mBinding.apply {
            // artwork image
            Glide
                .with(root)
                .load(mArgs.artworkUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageViewArtwork)

            // collapsing toolbar
            collapsingToolbarLayout.title = mArgs.podcastName

            // recycler view
            recyclerViewCollectionFeeds.adapter =
                CollectionFeedsAdapter(mViewModel)
        }
    }

    /* ------------------------------ Data */

    /**
     * Observe live data in view model
     */
    private fun observe() = lifecycleScope.launchWhenResumed {

        // collection data
        mViewModel.getCollection.observe(viewLifecycleOwner) {
            if (it.status == Status.SUCCESS) {
                // update the artwork image
                it.data?.artworkBigImageUrl?.also { imageUrl ->
                    Glide
                        .with(mBinding.root)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(mBinding.imageViewArtwork)
                }

                // update the collection
                it.data?.collectionName?.also { collectionName ->
                    mBinding.collapsingToolbarLayout.title = collectionName
                }

                // convert collection feed to view binding model
                mViewModel.convertCollectionFeedToBindingModel()
            }
        }

        // collection feed view binding model
        mViewModel.collectionBindingModels.observe(viewLifecycleOwner) {
            // submit new data source to adapter
            (mBinding.recyclerViewCollectionFeeds.adapter as? CollectionFeedsAdapter)
                ?.submitList(it)
        }
    }
}

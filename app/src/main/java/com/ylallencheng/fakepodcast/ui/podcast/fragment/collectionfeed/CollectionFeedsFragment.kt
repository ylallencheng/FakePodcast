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
import com.ylallencheng.fakepodcast.ui.podcast.fragment.CollectionFragmentArgs
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
    private val mArgs: CollectionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initUi()
        observe()
        return mBinding.root
    }

    private fun initUi() {
        mBinding.apply {
            Glide
                .with(root)
                .load(mArgs.artworkUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageViewArtwork)
            collapsingToolbarLayout.title = mArgs.podcastName
            recyclerViewCollectionFeeds.adapter =
                CollectionFeedsAdapter(
                    mViewModel
                )
        }
    }

    private fun observe() =
        lifecycleScope.launchWhenResumed {
            mViewModel.getCollection.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.artworkBigImageUrl?.also { imageUrl ->
                            Glide
                                .with(mBinding.root)
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(mBinding.imageViewArtwork)
                        }

                        it.data?.collectionName?.also { collectionName ->
                            mBinding.collapsingToolbarLayout.title = collectionName
                        }

                        mViewModel.convertCollectionToBindingModel()
                    }
                    else -> {
                    }
                }
            }

            mViewModel.collectionBindingModels.observe(viewLifecycleOwner) {
                (mBinding.recyclerViewCollectionFeeds.adapter as? CollectionFeedsAdapter)
                    ?.submitList(it)
            }
        }
}
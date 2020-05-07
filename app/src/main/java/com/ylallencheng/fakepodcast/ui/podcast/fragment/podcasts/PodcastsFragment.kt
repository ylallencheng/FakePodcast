package com.ylallencheng.fakepodcast.ui.podcast.fragment.podcasts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ylallencheng.fakepodcast.databinding.FragmentPodcastsBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
import com.ylallencheng.fakepodcast.io.model.Status
import com.ylallencheng.fakepodcast.ui.podcast.PodcastViewModel
import com.ylallencheng.fakepodcast.ui.podcast.fragment.PodcastsFragmentDirections
import com.ylallencheng.fakepodcast.util.observe
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PodcastsFragment : DaggerFragment() {

    // View Model
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: PodcastViewModel by activityViewModels { viewModelFactory }

    // View binding
    private val mBinding: FragmentPodcastsBinding by lazy {
        FragmentPodcastsBinding.inflate(layoutInflater)
    }

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
        mBinding.recyclerViewPodcasts.adapter =
            PodcastsAdapter(
                mViewModel
            )
    }

    private fun observe() =
        lifecycleScope.launchWhenResumed {
            mViewModel.getPodcasts.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        mViewModel.convertPodcastToBindingModel()
                    }
                    else -> {
                    }
                }
            }

            mViewModel.podcastBindingModels.observe(viewLifecycleOwner) {
                (mBinding.recyclerViewPodcasts.adapter as? PodcastsAdapter)?.submitList(it)
            }

            mViewModel.navigateToCollectionTrigger.observe(viewLifecycleOwner) {
                val navAction =
                    PodcastsFragmentDirections.actionPodcastsFragmentToCollectionFragment(
                        artworkUrl = it.artworkUrl,
                        artistName = it.artistName,
                        podcastName = it.podcastName
                    )
                findNavController().navigate(navAction)
            }
        }
}

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
        // recycler view
        mBinding.recyclerViewPodcasts.adapter =
            PodcastsAdapter(mViewModel)
    }

    /* ------------------------------ Data */

    /**
     * Observe live data in view model
     */
    private fun observe() = lifecycleScope.launchWhenResumed {

        // podcast data
        mViewModel.getPodcasts.observe(viewLifecycleOwner) {
            // convert data to view binding model if the status is success
            if (it.status == Status.SUCCESS) {
                mViewModel.convertPodcastToBindingModel()
            }
        }

        // podcast view binding models
        mViewModel.podcastBindingModels.observe(viewLifecycleOwner) {
            // submit new data source to adapter
            (mBinding.recyclerViewPodcasts.adapter as? PodcastsAdapter)?.submitList(it)
        }

        // navigating to collection
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

package com.ylallencheng.fakepodcast.ui.podcast.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ylallencheng.fakepodcast.R
import com.ylallencheng.fakepodcast.databinding.FragmentPodcastsBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
import com.ylallencheng.fakepodcast.util.observe
import com.ylallencheng.fakepodcast.io.model.Status
import com.ylallencheng.fakepodcast.ui.podcast.PodcastViewModel
import com.ylallencheng.fakepodcast.ui.podcast.adapter.PodcastsAdapter
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
        return mBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun initUi() {
        mBinding.recyclerViewPodcasts.adapter = PodcastsAdapter(mViewModel)
    }

    private fun observe() =
        lifecycleScope.launchWhenResumed {
            mViewModel.getPodcasts.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        mViewModel.convertPodcastToBindingModel()
                    }

                    Status.FAILED -> {

                    }
                }
            }

            mViewModel.podcastBindingModels.observe(viewLifecycleOwner) {
                (mBinding.recyclerViewPodcasts.adapter as? PodcastsAdapter)?.submitList(it)
            }

            mViewModel.navigateToCollectionTrigger.observe(viewLifecycleOwner) {
                findNavController().navigate(R.id.action_podcastsFragment_to_collectionFragment)
            }
        }
}

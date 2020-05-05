package com.ylallencheng.fakepodcast.ui.podcast.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope

import com.ylallencheng.fakepodcast.R
import com.ylallencheng.fakepodcast.databinding.FragmentCollectionBinding
import com.ylallencheng.fakepodcast.databinding.FragmentPodcastsBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
import com.ylallencheng.fakepodcast.extension.observe
import com.ylallencheng.fakepodcast.ui.podcast.PodcastViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CollectionFragment : DaggerFragment() {

    // View Model
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: PodcastViewModel by activityViewModels { viewModelFactory }

    // View binding
    private val mBinding: FragmentCollectionBinding by lazy {
        FragmentCollectionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() =
        lifecycleScope.launchWhenResumed {
            mViewModel.collections.observe(viewLifecycleOwner) {

            }
        }

}

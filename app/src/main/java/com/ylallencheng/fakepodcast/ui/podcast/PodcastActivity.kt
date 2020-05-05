package com.ylallencheng.fakepodcast.ui.podcast

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ylallencheng.fakepodcast.databinding.ActivityPodcastBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
import com.ylallencheng.fakepodcast.extension.observe
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class PodcastActivity : DaggerAppCompatActivity() {

    // View Model
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: PodcastViewModel by viewModels { viewModelFactory }

    // View binding
    private val mBinding: ActivityPodcastBinding by lazy {
        ActivityPodcastBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        observe()
    }

    private fun observe() =
        lifecycleScope.launchWhenResumed {
            mViewModel.podcasts.observe(this@PodcastActivity) {
                // submist list

            }

            mViewModel.collections.observe(this@PodcastActivity) {

            }
        }
}
package com.ylallencheng.fakepodcast.ui.podcast

import android.os.Bundle
import androidx.activity.viewModels
import com.ylallencheng.fakepodcast.databinding.ActivityPodcastBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
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
    }
}
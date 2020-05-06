package com.ylallencheng.fakepodcast.ui.podcast

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ylallencheng.fakepodcast.databinding.ActivityPodcastBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
import com.ylallencheng.fakepodcast.ui.player.PlayerActivity
import com.ylallencheng.fakepodcast.util.observe
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
            mViewModel.navigateToPlayerTrigger.observe(this@PodcastActivity) {
                PlayerActivity.navigate(
                    from = this@PodcastActivity,
                    title = it.feedName,
                    description = it.description,
                    contentUrl = it.contentUrl,
                    artworkUrl = mViewModel.getCollection.value?.data?.artworkBigImageUrl ?: ""
                )
            }
        }
}
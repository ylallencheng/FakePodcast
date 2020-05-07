package com.ylallencheng.fakepodcast.ui.player

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ylallencheng.fakepodcast.R
import com.ylallencheng.fakepodcast.databinding.ActivityPlayerBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
import com.ylallencheng.fakepodcast.util.formatPlaybackTimeLabel
import com.ylallencheng.fakepodcast.util.observe
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class PlayerActivity : DaggerAppCompatActivity() {

    // View Model
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: PlayerViewModel by viewModels { viewModelFactory }

    // View binding
    private val mBinding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    /* ------------------------------ Companion Object */

    companion object {

        /**
         * Util function for navigation to PlayerActivity
         */
        fun navigate(
            from: AppCompatActivity,
            title: String,
            description: String,
            contentUrl: String,
            artworkUrl: String
        ) {
            from.startActivity(Intent(from, PlayerActivity::class.java).apply {
                putExtra("title", title)
                putExtra("description", description)
                putExtra("contentUrl", contentUrl)
                putExtra("artworkUrl", artworkUrl)
            })
        }
    }

    /* ------------------------------ Lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initUi()
        setUpListeners()
        observe()
        startPlayerService()
    }

    /* ------------------------------ UI */

    /**
     * Initialize UI
     */
    private fun initUi() {
        intent?.extras?.also {
            Glide
                .with(mBinding.root)
                .load(it.getString("artworkUrl"))
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(mBinding.imageViewArtwork)
            mBinding.textViewTitle.text = it.getString("title")
        }
    }

    /**
     * Set up required listeners
     */
    private fun setUpListeners() {
        mBinding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    mViewModel.startSeeking(applicationContext)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    mViewModel.completeSeeking(applicationContext, seekBar?.progress ?: 0)
                }
            })

        mBinding.imageViewPausePlay.setOnClickListener {
            mViewModel.pausePlay(applicationContext)
        }

        mBinding.imageViewReplay.setOnClickListener {
            mViewModel.replay(applicationContext)
        }

        mBinding.imageViewForward.setOnClickListener {
            mViewModel.forward(applicationContext)
        }
    }

    /* ------------------------------ Data */

    /**
     * Observe live data in view model
     */
    private fun observe() = lifecycleScope.launchWhenResumed {
        // content total duration
        mViewModel.contentTotalDuration.observe(this@PlayerActivity) {
            // update seekbar max value and time label
            mBinding.seekBar.max = it
            mBinding.textViewTotalProgress.text = formatPlaybackTimeLabel(it.toLong())
        }

        // the playback state
        mViewModel.playing.observe(this@PlayerActivity) {
            // update image source to reveal playback state
            mBinding.imageViewPausePlay.setImageResource(if (it) R.drawable.pause_circle_filled_24px else R.drawable.play_arrow_24px)
        }

        // current playback position
        mViewModel.currentPosition.observe(this@PlayerActivity) {
            // update seekbar progress and time label
            mBinding.seekBar.progress = it
            mBinding.textViewCurrentProgress.text = formatPlaybackTimeLabel(it.toLong())
        }
    }

    /* ------------------------------ Service */

    /**
     * Start player service and start playing the podcast with given content url
     */
    private fun startPlayerService() {
        intent?.extras?.getString("contentUrl")?.also {
            mViewModel.startPlay(applicationContext, it)
        }
    }
}
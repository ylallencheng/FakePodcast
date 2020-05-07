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
import com.ylallencheng.fakepodcast.service.PlayerService
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

    companion object {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initUi()
        setUpListeners()
        observe()
        startPlayerService()
    }

    override fun onResume() {
        super.onResume()
        intent?.extras?.getString("contentUrl")?.also {
            mViewModel.startPlay(applicationContext, it)
        }
    }

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
                    mViewModel.startDragging(applicationContext)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    mViewModel.stopDragging(applicationContext, seekBar?.progress ?: 0)
                }
            })

        mBinding.imageViewPauseOrPlay.setOnClickListener {
            mViewModel.pausePlay(applicationContext)
        }

        mBinding.imageViewReplay.setOnClickListener {
            mViewModel.replay(applicationContext)
        }

        mBinding.imageViewForward.setOnClickListener {
            mViewModel.forward(applicationContext)
        }
    }

    private fun observe() = lifecycleScope.launchWhenResumed {
        mViewModel.contentTotalDuration.observe(this@PlayerActivity) {
            mBinding.seekBar.max = it
            mBinding.textViewTotalProgress.text = formatPlaybackTimeLabel(it.toLong())
        }

        mViewModel.playing.observe(this@PlayerActivity) {
            mBinding.imageViewPauseOrPlay.setImageResource(if (it) R.drawable.pause_circle_filled_24px else R.drawable.play_arrow_24px)
        }

        mViewModel.currentPosition.observe(this@PlayerActivity) {
            mBinding.seekBar.progress = it
            mBinding.textViewCurrentProgress.text = formatPlaybackTimeLabel(it.toLong())
        }
    }

    private fun startPlayerService() {
        val intent = Intent(applicationContext, PlayerService::class.java)
        applicationContext.startService(intent)
    }
}
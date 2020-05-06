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
        observe()
    }

    private fun initUi() {
        mBinding.apply {
            intent?.extras?.also {
                Glide
                    .with(root)
                    .load(it.getString("artworkUrl"))
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageViewArtwork)

                textViewTitle.text = it.getString("title")
            }

            seekBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        mViewModel.progressChanged()
                        // todo: update textViewCurrent
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        mViewModel.startDragging()
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        mViewModel.stopDragging()
                    }
                })

            imageViewPauseOrPlay.setOnClickListener {
                mViewModel.playOrPause()

            }

            imageViewReplay.setOnClickListener {
                mViewModel.replay()
            }

            imageViewForward.setOnClickListener {
                mViewModel.forward()
            }
        }
    }

    private fun observe() =
        lifecycleScope.launchWhenResumed {
            mViewModel.playing.observe(this@PlayerActivity) {
                mBinding.imageViewPauseOrPlay.setImageResource(if (it) R.drawable.pause_circle_filled_24px else R.drawable.play_arrow_24px)
            }
        }
}
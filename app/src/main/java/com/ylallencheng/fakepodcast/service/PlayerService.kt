package com.ylallencheng.fakepodcast.service

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.SEEK_CLOSEST
import android.os.Build
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.ylallencheng.fakepodcast.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

class PlayerService :
    LifecycleService(),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var mContentUrl: String? = null
    private var mTimerJob = lifecycleScope.launchWhenCreated {
        withContext(Dispatchers.Default) {
            while (true) {
                mMediaPlayer?.also {
                    if (it.isPlaying) {
                        if (it.currentPosition <= it.duration) {
                            postUpdatePlaybackCurrentPosition(it.currentPosition)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_START_PLAY = "action.start.play"
        const val ACTION_PAUSE = "action.pause"
        const val ACTION_RESUME = "action.resume"
        const val ACTION_REPLAY = "action.replay"
        const val ACTION_FORWARD = "action.forward"
        const val ACTION_SEEK_TO = "action.seek.to"

        val playbackCurrentPosition: SingleLiveEvent<Int> = SingleLiveEvent()
        val playbackTotalDuration: SingleLiveEvent<Int> = SingleLiveEvent()
        val isPlaybackPlaying: SingleLiveEvent<Boolean> = SingleLiveEvent()

        fun startPlay(context: Context, url: String) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply {
                    action = ACTION_START_PLAY
                    putExtra("url", url)
                })

        fun pause(context: Context) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply { action = ACTION_PAUSE })

        fun resume(context: Context) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply { action = ACTION_RESUME })

        fun replay(context: Context) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply { action = ACTION_REPLAY })

        fun forward(context: Context) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply { action = ACTION_FORWARD })

        fun seekTo(context: Context, position: Int) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply {
                    action = ACTION_SEEK_TO
                    putExtra("position", position)
                })
    }

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setOnPreparedListener(this@PlayerService)
            setOnCompletionListener(this@PlayerService)
        }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        super.onStartCommand(intent, flags, startId)
        mMediaPlayer?.also { player ->
            when (intent?.action) {
                ACTION_START_PLAY -> {
                    val url = intent.extras?.getString("url")
                    when {
                        url?.equals(mContentUrl, ignoreCase = true) == true -> {
                            updatePlaybackState(player.isPlaying)
                        }

                        else -> {
                            player.reset()
                            updatePlaybackState(player.isPlaying)
                            if (url?.isNotEmpty() == true) {
                                player.setDataSource(url)
                                player.prepareAsync()
                                mContentUrl = url
                            }
                        }
                    }
                }

                ACTION_PAUSE -> {
                    if (player.isPlaying) {
                        player.pause()
                        updatePlaybackState(player.isPlaying)
                    }
                }

                ACTION_RESUME -> {
                    if (!player.isPlaying) {
                        player.start()
                        updatePlaybackState(player.isPlaying)
                    }
                }

                ACTION_REPLAY -> {
                    seekTo(player, max(0, player.currentPosition - 30L * 1000).toInt())
                }

                ACTION_FORWARD -> {
                    seekTo(player, min(player.duration, player.currentPosition + 30 * 1000))
                }

                ACTION_SEEK_TO -> {
                    seekTo(player, intent.extras?.getInt("position") ?: player.currentPosition)
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.release()
        mMediaPlayer = null
        mTimerJob.cancel()
        mContentUrl = null
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.also {
            it.start()
            updatePlaybackTotalDuration(it.duration)
            updatePlaybackState(it.isPlaying)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        updatePlaybackState(false)
        updatePlaybackCurrentPosition(0)
    }

    @MainThread
    private fun updatePlaybackState(playing: Boolean) {
        isPlaybackPlaying.value = playing
    }

    @MainThread
    private fun updatePlaybackTotalDuration(duration: Int) {
        playbackTotalDuration.value = duration
    }

    @MainThread
    private fun updatePlaybackCurrentPosition(position: Int) {
        playbackCurrentPosition.value = position
    }

    private fun postUpdatePlaybackCurrentPosition(position: Int) {
        playbackCurrentPosition.postValue(position)
    }

    @MainThread
    private fun seekTo(
        player: MediaPlayer,
        position: Int
    ) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                player.seekTo(position.toLong(), SEEK_CLOSEST)
            else ->
                player.seekTo(position)
        }
        updatePlaybackCurrentPosition(position)
    }
}
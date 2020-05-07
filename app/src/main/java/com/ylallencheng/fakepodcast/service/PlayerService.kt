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

/**
 * Service for hosting MediaPlayer
 */
class PlayerService :
    LifecycleService(),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener {

    /*
        The media player
     */
    private var mMediaPlayer: MediaPlayer? = null

    /*
        The content url
     */
    private var mContentUrl: String? = null

    /*
        The timer job which is used for updating current position
     */
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

    /* ------------------------------ Companion Object */

    companion object {

        // Service Actions
        const val ACTION_START_PLAY = "action.start.play"
        const val ACTION_PAUSE = "action.pause"
        const val ACTION_RESUME = "action.resume"
        const val ACTION_REPLAY = "action.replay"
        const val ACTION_FORWARD = "action.forward"
        const val ACTION_SEEK_TO = "action.seek.to"

        /*
            Playback current position
         */
        val playbackCurrentPosition: SingleLiveEvent<Int> = SingleLiveEvent()

        /*
            Playback total duration
         */
        val playbackTotalDuration: SingleLiveEvent<Int> = SingleLiveEvent()

        /*
            Playback is currently playing or not
         */
        val isPlaybackPlaying: SingleLiveEvent<Boolean> = SingleLiveEvent()

        /**
         * Start playing with given url
         */
        fun startPlay(context: Context, url: String) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply {
                    action = ACTION_START_PLAY
                    putExtra("url", url)
                })

        /**
         * Pause playback
         */
        fun pause(context: Context) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply { action = ACTION_PAUSE })

        /**
         * Resume playback
         */
        fun resume(context: Context) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply { action = ACTION_RESUME })

        /**
         * Replay playback with 30s
         */
        fun replay(context: Context) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply { action = ACTION_REPLAY })

        /**
         * Forward playback with 30s
         */
        fun forward(context: Context) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply { action = ACTION_FORWARD })

        /**
         * Seek playback to given position
         */
        fun seekTo(context: Context, position: Int) =
            context.startService(Intent(context, PlayerService::class.java)
                .apply {
                    action = ACTION_SEEK_TO
                    putExtra("position", position)
                })
    }

    /* ------------------------------ Lifecycle */

    override fun onCreate() {
        super.onCreate()
        // initialize media player
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

    override fun onDestroy() {
        super.onDestroy()
        // release all captured resources
        mMediaPlayer?.release()
        mMediaPlayer = null
        mTimerJob.cancel()
        mContentUrl = null
    }

    /* ------------------------------ Overrides */

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        super.onStartCommand(intent, flags, startId)
        mMediaPlayer?.also { player ->
            when (intent?.action) {
                /*
                    Start Play
                 */
                ACTION_START_PLAY -> {
                    // get the content url
                    val url = intent.extras?.getString("url")
                    when {
                        // same content url, just update playback state
                        url?.equals(mContentUrl, ignoreCase = true) == true -> {
                            updatePlaybackState(player.isPlaying)
                        }

                        // new content url
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

                /*
                    Pause
                 */
                ACTION_PAUSE -> {
                    if (player.isPlaying) {
                        player.pause()
                        updatePlaybackState(player.isPlaying)
                    }
                }

                /*
                    Resume
                 */
                ACTION_RESUME -> {
                    if (!player.isPlaying) {
                        player.start()
                        updatePlaybackState(player.isPlaying)
                    }
                }

                /*
                    Replay
                 */
                ACTION_REPLAY -> {
                    seekTo(player, max(0, player.currentPosition - 30L * 1000).toInt())
                }

                /*
                    Forward
                 */
                ACTION_FORWARD -> {
                    seekTo(player, min(player.duration, player.currentPosition + 30 * 1000))
                }

                /*
                    Seek To
                 */
                ACTION_SEEK_TO -> {
                    seekTo(player, intent.extras?.getInt("position") ?: player.currentPosition)
                }
            }
        }
        return START_STICKY
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

    /* ------------------------------ Base Methods */

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
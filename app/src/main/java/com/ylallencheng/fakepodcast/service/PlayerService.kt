package com.ylallencheng.fakepodcast.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.SEEK_CLOSEST
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.ylallencheng.fakepodcast.util.SingleLiveEvent
import kotlin.math.max
import kotlin.math.min

class PlayerService :
    Service(),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var mContentUrl: String? = null

    companion object {
        const val ACTION_START_PLAY = "action.start.play"
        const val ACTION_PAUSE = "action.pause"
        const val ACTION_RESUME = "action.resume"
        const val ACTION_REPLAY = "action.replay"
        const val ACTION_FORWARD = "action.forward"

        val duration: MutableLiveData<Int> = MutableLiveData()
        val playing: SingleLiveEvent<Boolean> = SingleLiveEvent()

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
    }

    override fun onBind(intent: Intent?): IBinder? = null

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
        }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        mMediaPlayer?.also { player ->

            when (intent?.action) {
                ACTION_START_PLAY -> {
                    val url = intent.extras?.getString("url")
                    when {
                        url?.equals(mContentUrl, ignoreCase = true) == true -> {
                            playing.postValue(player.isPlaying)
                        }

                        else -> {
                            player.reset()
                            playing.postValue(player.isPlaying)
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
                        playing.postValue(player.isPlaying)
                    }
                }

                ACTION_RESUME -> {
                    if (!player.isPlaying) {
                        player.start()
                        playing.postValue(player.isPlaying)
                    }
                }

                ACTION_REPLAY -> {
                    val finalPosition = max(0, player.currentPosition - 30L * 1000)
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                            player.seekTo(finalPosition, SEEK_CLOSEST)
                        else ->
                            player.seekTo(finalPosition.toInt())
                    }
                }

                ACTION_FORWARD -> {
                    val finalPosition = min(player.duration, player.currentPosition + 30 * 1000)
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                            player.seekTo(finalPosition.toLong(), SEEK_CLOSEST)
                        else ->
                            player.seekTo(finalPosition)
                    }
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.also {
            it.start()
            duration.postValue(it.duration)
            playing.postValue(it.isPlaying)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playing.postValue(false)
    }
}
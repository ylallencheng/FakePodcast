package com.ylallencheng.fakepodcast.ui.player

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.ylallencheng.fakepodcast.service.PlayerService
import javax.inject.Inject

/**
 * The ViewModel of PlayerActivity
 */
class PlayerViewModel @Inject constructor() : ViewModel() {

    /*
        Flag to indicate whether the playback state was force changed (by user)
     */
    private var mPlaybackStateForceChanged: Boolean = false

    /*
        Current playback position
     */
    val currentPosition: LiveData<Int> = PlayerService.playbackCurrentPosition.map { it }

    /*
        Total playback duration
     */
    val contentTotalDuration: LiveData<Int> = PlayerService.playbackTotalDuration.map { it }

    /*
        Current playback state
     */
    val playing: LiveData<Boolean> = PlayerService.isPlaybackPlaying.map { it }

    /**
     * Start playing the podcast with given content url
     */
    fun startPlay(
        applicationContext: Context,
        contentUrl: String
    ) {
        PlayerService.startPlay(applicationContext, contentUrl)
    }

    /**
     * Pause or resume playing the podcast
     */
    fun pausePlay(applicationContext: Context) {
        when (playing.value) {
            true -> PlayerService.pause(applicationContext)
            false -> PlayerService.resume(applicationContext)
        }
    }

    /**
     * Replay the playback with 30s
     */
    fun replay(applicationContext: Context) {
        PlayerService.replay(applicationContext)
    }

    /**
     * Forward the playback with 30s
     */
    fun forward(applicationContext: Context) {
        PlayerService.forward(applicationContext)
    }

    /**
     * User start seeking the playback position
     */
    fun startSeeking(applicationContext: Context) {
        // if playback is in playing state, then pause the playback and mark it as force changed
        if (playing.value == true) {
            mPlaybackStateForceChanged = true
            PlayerService.pause(applicationContext)
        }
    }

    /**
     * User finish seeking the playback position
     */
    fun completeSeeking(
        applicationContext: Context,
        latestPosition: Int
    ) {
        // if the playback was force changed, resume playing
        if (mPlaybackStateForceChanged) {
            PlayerService.resume(applicationContext)
        }
        mPlaybackStateForceChanged = false
        PlayerService.seekTo(applicationContext, latestPosition)
    }
}
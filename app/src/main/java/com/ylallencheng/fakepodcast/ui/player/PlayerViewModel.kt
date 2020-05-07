package com.ylallencheng.fakepodcast.ui.player

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.ylallencheng.fakepodcast.service.PlayerService
import javax.inject.Inject

class PlayerViewModel @Inject constructor() : ViewModel() {

    private var playingStatusForceChanged: Boolean = false

    val currentPosition: LiveData<Int> = PlayerService.playbackCurrentPosition.map { it }
    val contentTotalDuration: LiveData<Int> = PlayerService.playbackTotalDuration.map { it }
    val playing: LiveData<Boolean> = PlayerService.isPlaybackPlaying.map { it }

    fun startDragging(applicationContext: Context) {
        if (playing.value == true) {
            playingStatusForceChanged = true
            PlayerService.pause(applicationContext)
        }
    }

    fun stopDragging(
        applicationContext: Context,
        latestPosition: Int
    ) {
        if (playingStatusForceChanged) {
            PlayerService.resume(applicationContext)
        }
        playingStatusForceChanged = false
        PlayerService.seekTo(applicationContext, latestPosition)
    }

    fun startPlay(
        applicationContext: Context,
        contentUrl: String
    ) {
        PlayerService.startPlay(applicationContext, contentUrl)
    }

    fun pausePlay(applicationContext: Context) {
        when (playing.value) {
            true -> PlayerService.pause(applicationContext)
            false -> PlayerService.resume(applicationContext)
        }
    }

    fun replay(applicationContext: Context) {
        PlayerService.replay(applicationContext)
    }

    fun forward(applicationContext: Context) {
        PlayerService.forward(applicationContext)
    }
}
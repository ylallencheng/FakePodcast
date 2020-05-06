package com.ylallencheng.fakepodcast.ui.player

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.ylallencheng.fakepodcast.service.PlayerService
import javax.inject.Inject

class PlayerViewModel @Inject constructor() : ViewModel() {

    var dragging: Boolean = false
    var playingStatusForceChanged: Boolean = false

    val contentTotalDuration: LiveData<Int> = PlayerService.duration.map { it }
    val playing: LiveData<Boolean> = PlayerService.playing.map { it }

    fun startDragging() {
        dragging = true
    }

    fun stopDragging() {
        dragging = false
        if (playingStatusForceChanged) {
        }
        playingStatusForceChanged = false
    }

    fun progressChanged() {

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
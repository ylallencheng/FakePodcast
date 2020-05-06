package com.ylallencheng.fakepodcast.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class PlayerViewModel @Inject constructor() : ViewModel() {

    var dragging: Boolean = false
    var playingStatusForceChanged: Boolean = false
    var playing: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }

    fun startDragging() {
        dragging = true
        if (playing.value == true) {
            playingStatusForceChanged = true
            playOrPause()
        }
    }

    fun stopDragging() {
        dragging = false
        if (playingStatusForceChanged) {
            playOrPause()
        }
        playingStatusForceChanged = false
    }

    fun progressChanged() {

    }

    fun playOrPause() {
        playing.value = playing.value?.not() ?: false
    }

    fun replay() {

    }

    fun forward() {

    }
}
@file:JvmName("LiveDataUtil")

package com.ylallencheng.fakepodcast.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.TimeUnit

/**
 * Kt extension for simplify livedata-observe boilerplate
 */
fun <T> LiveData<T>.observe(
    lifecycleOwner: LifecycleOwner,
    onChange: (T) -> Unit
) =
    observe(lifecycleOwner, Observer {
        onChange(it)
    })

/**
 * Format playback time label
 */
fun formatPlaybackTimeLabel(duration: Long) =
    String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(duration),
        TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
    )
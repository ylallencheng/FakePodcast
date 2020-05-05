@file:JvmName("LiveDataUtil")

package com.ylallencheng.fakepodcast.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(
    lifecycleOwner: LifecycleOwner,
    onChange: (T) -> Unit
) =
    observe(lifecycleOwner, Observer {
        onChange(it)
    })
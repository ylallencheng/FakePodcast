package com.ylallencheng.fakepodcast

import com.ylallencheng.fakepodcast.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Extend DaggerApplication for implementing Dagger2
 */
class FPApplication : DaggerApplication() {

    /* ------------------------------ Overrides */

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)
}
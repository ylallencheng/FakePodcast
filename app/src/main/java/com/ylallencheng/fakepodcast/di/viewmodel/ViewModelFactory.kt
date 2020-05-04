package com.ylallencheng.fakepodcast.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val mViewModelMap: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(viewModelClass: Class<T>): T {
        var provider = mViewModelMap[viewModelClass]

        // check the null view model is a superclass or superinterface
        if (provider == null) {
            for ((key, value) in mViewModelMap) {
                if (viewModelClass.isAssignableFrom(key)) {
                    provider = value
                    break
                }
            }
        }

        // if view model is not in view model map or
        // not a superclass or superinterface of the view model, throw exception
        requireNotNull(provider) { "unknown model class $viewModelClass" }

        try {
            @Suppress("UNCHECKED_CAST")
            return provider.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
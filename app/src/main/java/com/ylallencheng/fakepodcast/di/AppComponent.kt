package com.ylallencheng.fakepodcast.di

import com.ylallencheng.fakepodcast.FPApplication
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactoryModule
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<FPApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: FPApplication): AppComponent
    }
}
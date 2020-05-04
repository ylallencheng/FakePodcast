package com.ylallencheng.fakepodcast.di

import androidx.room.Room
import com.ylallencheng.fakepodcast.BuildConfig
import com.ylallencheng.fakepodcast.FPApplication
import com.ylallencheng.fakepodcast.R
import com.ylallencheng.fakepodcast.di.player.PlayerActivityModule
import com.ylallencheng.fakepodcast.di.podcast.PodcastActivityModule
import com.ylallencheng.fakepodcast.io.db.CollectionDao
import com.ylallencheng.fakepodcast.io.db.FPDatabase
import com.ylallencheng.fakepodcast.io.db.PodcastDao
import com.ylallencheng.fakepodcast.io.network.PodcastService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(
    includes = [
        PodcastActivityModule::class,
        PlayerActivityModule::class
    ]
)
class AppModule {

    @Singleton
    @Provides
    fun providePodcastService(application: FPApplication): PodcastService =
        Retrofit.Builder()
            .baseUrl(application.getString(R.string.base_url))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(5L, TimeUnit.SECONDS)
                    readTimeout(5L, TimeUnit.SECONDS)

                    // write log in debug mode
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                    }
                }.build()
            )
            .build()
            .create(PodcastService::class.java)

    @Singleton
    @Provides
    fun provideFPDatabase(application: FPApplication): FPDatabase =
        Room
            .databaseBuilder(
                application.applicationContext,
                FPDatabase::class.java,
                application.packageName
            )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providePodcastDao(database: FPDatabase): PodcastDao = database.podcastDao()

    @Singleton
    @Provides
    fun provideCollectionDao(database: FPDatabase): CollectionDao = database.collectionDao()
}
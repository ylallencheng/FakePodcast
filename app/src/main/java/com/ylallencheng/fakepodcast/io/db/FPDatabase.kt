package com.ylallencheng.fakepodcast.io.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ylallencheng.fakepodcast.io.model.Collection
import com.ylallencheng.fakepodcast.io.model.Podcast

@Database(
    entities = [
        Podcast::class,
        Collection::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        FPDbTypeConverter::class
    ]
)
abstract class FPDatabase : RoomDatabase() {

    abstract fun podcastDao(): PodcastDao

    abstract fun collectionDao(): CollectionDao
}

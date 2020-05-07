package com.ylallencheng.fakepodcast.io.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ylallencheng.fakepodcast.io.model.Collection
import com.ylallencheng.fakepodcast.io.model.Podcast

@Dao
interface PodcastDao {

    @Query("SELECT * FROM Podcast")
    fun getAllPodcasts(): List<Podcast>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPodcasts(podcasts: List<Podcast>)
}

@Dao
interface CollectionDao {
    @Query("SELECT * FROM Collection WHERE collectionId == :collectionId")
    fun getCollection(collectionId: Int): Collection?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: Collection)
}
package com.ylallencheng.fakepodcast.io.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ylallencheng.fakepodcast.io.model.Collection
import com.ylallencheng.fakepodcast.io.model.Podcast

@Dao
interface PodcastDao {

    @Query("SELECT * FROM Podcast")
    fun getAllPodcasts(): LiveData<List<Podcast>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPodcasts(podcasts: List<Podcast>)
}

@Dao
interface CollectionDao {
    @Query("SELECT * FROM Collection WHERE collectionId == :collectionId")
    fun getCollection(collectionId: Int): LiveData<Collection>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCollections(collections: List<Collection>)
}
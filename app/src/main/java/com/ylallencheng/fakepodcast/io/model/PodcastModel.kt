package com.ylallencheng.fakepodcast.io.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

/* ------------------------------ Base Response */

data class Response<T>(val data: T?)

/* ------------------------------ Podcast */

data class Podcasts(@field:Json(name = "podcast") val podcasts: List<Podcast>?)

@Entity
data class Podcast(
    @PrimaryKey val id: String,
    val name: String?,
    val artistName: String?,
    @field:Json(name = "artworkUrl100") val artworkImageUrl: String?
)

/* ------------------------------ Collection */

data class CollectionData(val collection: Collection?)

@Entity
data class Collection(
    @field:Json(name = "artworkUrl600") val artworkBigImageUrl: String?,
    @PrimaryKey val collectionId: Int,
    val collectionName: String?,
    val contentFeed: List<CollectionFeed>?
)

data class CollectionFeed(
    val contentUrl: String?,
    val description: String?,
    val title: String?
) {
    companion object {
        val DB_CONVERTER_TYPE: ParameterizedType =
            Types.newParameterizedType(List::class.java, CollectionFeed::class.java)
    }
}



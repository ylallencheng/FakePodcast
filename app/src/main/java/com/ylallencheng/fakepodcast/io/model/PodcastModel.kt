package com.ylallencheng.fakepodcast.io.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.Types

data class Response<T>(val data: T?)

data class Podcasts(@field:Json(name = "podcast") val podcasts: List<Podcast>?)

@Entity
data class Podcast(
    @PrimaryKey val id: String,
    val name: String?,
    val artistName: String?,
    @field:Json(name = "artworkUrl100") val artworkImageUrl: String?
)

data class CollectionData(val collection: Collection?)

@Entity
data class Collection(
    val artistId: Int?,
    val artistName: String?,
    @field:Json(name = "artworkUrl100") val artworkImageUrl: String?,
    @field:Json(name = "artworkUrl600") val artworkBigImageUrl: String?,
    @PrimaryKey val collectionId: Int,
    val collectionName: String?,
    val contentFeed: List<CollectionFeed>?,
    val country: String?,
    val genreIds: String?,
    val genres: String?,
    val releaseDate: String?
) {
    companion object {
        const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }
}

data class CollectionFeed(
    val contentUrl: String?,
    val description: String?,
    val publishedDate: String?,
    val title: String?
) {
    companion object {
        const val DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z"

        val DB_CONVERTER_TYPE =
            Types.newParameterizedType(List::class.java, CollectionFeed::class.java)
    }
}



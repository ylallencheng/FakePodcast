package com.ylallencheng.fakepodcast.io.db

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.ylallencheng.fakepodcast.io.model.CollectionFeed

class FPDbTypeConverter {

    private val mMoshi = Moshi.Builder().build()

    @TypeConverter
    fun convertContentFeedToJsonString(list: List<CollectionFeed>?): String =
        list
            ?.let {
                mMoshi.adapter<List<CollectionFeed>>(CollectionFeed.DB_CONVERTER_TYPE).toJson(it)
            }
            ?: ""

    @TypeConverter
    fun convertJsonStringToContentFeed(jsonString: String): List<CollectionFeed>? =
        mMoshi.adapter<List<CollectionFeed>>(CollectionFeed.DB_CONVERTER_TYPE).fromJson(jsonString)
}
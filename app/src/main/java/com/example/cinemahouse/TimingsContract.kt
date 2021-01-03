package com.example.cinemahouse

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object TimingsContract {

    internal const val TABLE_NAME = "Timings"

    /**
     * The URI to access the Movies table.
     */
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"


    // Tasks fields
    object Columns {
        const val ID = BaseColumns._ID
        const val TIMING_MOVIE_ID = "MovieID"
        const val TIMING_RELEASE_DATE = "ReleaseDate"
        const val TIMING_DURATION = "Duration"
    }

    fun getId(uri: Uri): Long {
        return ContentUris.parseId(uri)
    }

    fun buildUriFromId(id: Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }
}
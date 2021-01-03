package com.example.cinemahouse

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object MoviesContract {

    internal const val TABLE_NAME = "Movies"

    /**
     * The URI to access the Movies table.
     */
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"


    // Tasks fields
    object Columns {
        const val ID = BaseColumns._ID
        const val MOVIE_NAME = "Name"
        const val MOVIE_GENRE = "Genre"
        const val MOVIE_RATINGS = "Ratings"
    }

    fun getId(uri: Uri): Long {
        return ContentUris.parseId(uri)
    }

    fun buildUriFromId(id: Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }
}
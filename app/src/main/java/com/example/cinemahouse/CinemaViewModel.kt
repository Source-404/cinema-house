package com.example.cinemahouse

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import android.app.Application
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val TAG = "CinemaViewModel"

class CinemaViewModel(application: Application) : AndroidViewModel(application)
{
    private val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            Log.d(TAG, "contentObserver.onChange: called. uri is $uri")
            loadMovie()
        }
    }

    private val databaseCursor = MutableLiveData<Cursor>()
    val cursor: LiveData<Cursor>
        get() = databaseCursor

    init {
        Log.d(TAG, "CinemaViewModel: created")
        getApplication<Application>().contentResolver.registerContentObserver(MoviesContract.CONTENT_URI,
            true, contentObserver)
        loadMovie()
    }

    private fun loadMovie() {

        val projection = arrayOf(
            MoviesContract.Columns.ID,
            MoviesContract.Columns.MOVIE_NAME,
            MoviesContract.Columns.MOVIE_GENRE,
            MoviesContract.Columns.MOVIE_RATINGS
        )
        // <order by> Movie.Ratings, Movie.Name
        val rating = "${MoviesContract.Columns.MOVIE_RATINGS}, ${MoviesContract.Columns.MOVIE_NAME}"

        GlobalScope.launch {
            val cursor = getApplication<Application>().contentResolver.query(
                MoviesContract.CONTENT_URI,
                projection, null, null,
                rating
            )
            databaseCursor.postValue(cursor)
        }
    }

    fun deleteMovie(movieId: Long) {
        GlobalScope.launch {
            getApplication<Application>().contentResolver?.delete(
                MoviesContract.buildUriFromId(
                    movieId
                ), null, null
            )
        }
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: called")
        getApplication<Application>().contentResolver.unregisterContentObserver(contentObserver)
    }

}
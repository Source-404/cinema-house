package com.example.cinemahouse

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Basic database class for the application.
 *
 * The only class that should use this is [AppProvider].
 */

private const val TAG = "AppDatabase"

private const val DATABASE_NAME = "CinemaHouse.db"
private const val DATABASE_VERSION = 1

internal class AppDatabase private constructor(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        Log.d(TAG, "AppDatabase: initialising")
    }

    override fun onCreate(db: SQLiteDatabase) {
        // CREATE TABLE Tasks (_id INTEGER PRIMARY KEY NOT NULL, Name TEXT NOT NULL, Description TEXT, SortOrder INTEGER);
        Log.d(TAG, "onCreate: starts")
        val sSQL = """CREATE TABLE ${MoviesContract.TABLE_NAME} (
            ${MoviesContract.Columns.ID} INTEGER PRIMARY KEY NOT NULL,
            ${MoviesContract.Columns.MOVIE_NAME} TEXT NOT NULL,
            ${MoviesContract.Columns.MOVIE_GENRE} TEXT,
            ${MoviesContract.Columns.MOVIE_RATINGS} INTEGER);""".replaceIndent(" ")
        Log.d(TAG, sSQL)
        db.execSQL(sSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: starts")
        when(oldVersion) {
            1 -> {
                // upgrade logic from version 1
            }
            else -> throw IllegalStateException("onUpgrade() with unknown newVersion: $newVersion")
        }
    }

    companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase)
}
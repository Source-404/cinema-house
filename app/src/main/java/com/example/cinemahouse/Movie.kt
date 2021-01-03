package com.example.cinemahouse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Movie(val name: String, val genre: String, val rating: Int) : Parcelable {
    var id: Long = 0
}
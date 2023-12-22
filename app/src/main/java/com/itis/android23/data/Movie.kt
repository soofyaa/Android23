package com.itis.android23.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String = "",
    var genre: String = "",
    var releaseYear: Int = 1000,
    var description: String = "",
    var urlCover: String = ""
)


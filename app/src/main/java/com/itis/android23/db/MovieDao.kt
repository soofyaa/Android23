package com.itis.android23.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.itis.android23.data.Movie

@Dao
interface MovieDao {

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE title = :title AND releaseYear = :releaseYear")
    suspend fun getMovieByTitleAndReleaseYear(title: String, releaseYear: Int): Movie?

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Long): Movie?

    @Delete
    suspend fun deleteMovie(movie: Movie)
}


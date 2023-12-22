package com.itis.android23.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.itis.android23.data.Movie
import com.itis.android23.holder.MovieViewHolder

class MovieAdapter(
    private var movies: List<Movie>,
    private val onMovieClickListener: (Movie) -> Unit,
    private val onMovieDeleteListener: (Movie) -> Unit
) : RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies.sortedByDescending { it.releaseYear }
        notifyDataSetChanged()
    }

    fun deleteMovie(position: Int) {
        val deletedMovie = movies[position]
        onMovieDeleteListener(deletedMovie)
    }
}




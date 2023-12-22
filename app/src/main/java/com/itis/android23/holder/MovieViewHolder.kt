package com.itis.android23.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itis.android23.R
import com.itis.android23.data.Movie

class MovieViewHolder(itemView: View, private val onMovieClickListener: (Movie) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
    private val yearTextView: TextView = itemView.findViewById(R.id.textViewYear)

    private lateinit var currentMovie: Movie

    init {
        itemView.setOnClickListener {
            if (::currentMovie.isInitialized) {
                onMovieClickListener(currentMovie)
            }
        }
    }

    fun bind(movie: Movie) {
        currentMovie = movie
        Glide.with(itemView)
            .load(movie.urlCover)
            .into(imageView)

        titleTextView.text = movie.title
        yearTextView.text = movie.releaseYear.toString()
    }

    companion object {
        fun create(parent: ViewGroup, onMovieClickListener: (Movie) -> Unit): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)

            return MovieViewHolder(view, onMovieClickListener)
        }
    }
}


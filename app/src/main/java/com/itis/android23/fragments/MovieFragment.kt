package com.itis.android23.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.android23.R
import com.itis.android23.data.Movie
import com.itis.android23.databinding.FragmentMovieBinding
import com.itis.android23.db.AppDatabase
import com.itis.android23.db.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieFragment : Fragment(R.layout.fragment_movie) {

    private val binding by viewBinding(FragmentMovieBinding::bind)
    private val movieDao: MovieDao by lazy {
        AppDatabase.getInstance(requireContext()).movieDao()
    }
    private var movieId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE

        arguments?.let {
            movieId = it.getLong("movieId")
        }

        lifecycleScope.launch(Dispatchers.Main) {
            val movie = movieDao.getMovieById(movieId)

            // Отображаем данные фильма
            movie?.let {
                displayMovieDetails(it)
            }
        }
    }

    private fun displayMovieDetails(movie: Movie) {
        binding.textTitle.text = movie.title
        binding.textGenre.text = movie.genre
        binding.textReleaseYear.text = movie.releaseYear.toString()
        binding.textDescription.text = movie.description
        Glide.with(this).load(movie.urlCover).into(binding.imageViewCover)
    }
}

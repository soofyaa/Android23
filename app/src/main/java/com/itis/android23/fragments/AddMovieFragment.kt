package com.itis.android23.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.android23.R
import com.itis.android23.data.Movie
import com.itis.android23.databinding.FragmentAddMovieBinding
import com.itis.android23.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMovieFragment : Fragment(R.layout.fragment_add_movie) {

    private val binding by viewBinding(FragmentAddMovieBinding::bind)
    private val movieFormData = Movie()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddMovie.setOnClickListener {
            updateMovieFormData()
            addMovieToDatabase()
        }
    }

    private fun updateMovieFormData() {
        with(binding) {
            movieFormData.title = editTextTitle.text.toString()
            movieFormData.genre = editTextGenre.text.toString()
            movieFormData.releaseYear = editTextReleaseYear.text.toString().toIntOrNull() ?: 0
            movieFormData.description = editTextDescription.text.toString()
            movieFormData.urlCover = editTextUrlCover.text.toString()
        }
    }

    private fun addMovieToDatabase() {
        val (id, title, genre, releaseYear, description, urlCover) = movieFormData

        if (binding.editTextTitle.text.isNotEmpty() && binding.editTextGenre.text.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                val existingMovie = AppDatabase.getInstance(requireContext())
                    .movieDao().getMovieByTitleAndReleaseYear(title, releaseYear)

                withContext(Dispatchers.Main) {
                    handleMovieDatabaseResult(existingMovie, title, genre, releaseYear, description, urlCover)
                }
            }
        }
    }

    private suspend fun handleMovieDatabaseResult(
        existingMovie: Movie?,
        title: String,
        genre: String,
        releaseYear: Int,
        description: String,
        urlCover: String
    ) {
        if (existingMovie == null) {
            val movie = Movie(
                title = title,
                genre = genre,
                releaseYear = releaseYear,
                description = description,
                urlCover = urlCover
            )

            AppDatabase.getInstance(requireContext()).movieDao().insertMovie(movie)
            showToast(getString(R.string.movie_saved))
        } else {
            showToast(getString(R.string.the_movie_with_the_same_title_and_release_year_already_exists))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

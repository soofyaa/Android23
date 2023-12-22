package com.itis.android23.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.android23.R
import com.itis.android23.adapter.MovieAdapter
import com.itis.android23.data.Movie
import com.itis.android23.databinding.FragmentMainBinding
import com.itis.android23.db.AppDatabase
import com.itis.android23.db.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(emptyList(), { movie ->
            navigateToMovieDetailsFragment(movie)
        }, { deletedMovie ->
            deleteMovieFromDatabase(deletedMovie)
        })
    }

    private val movieDao: MovieDao by lazy {
        AppDatabase.getInstance(requireContext()).movieDao()
    }

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE

        binding.recyclerViewAllMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAllMovies.adapter = movieAdapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                movieAdapter.deleteMovie(position)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerViewAllMovies)

        lifecycleScope.launch(Dispatchers.Main) {
            val movies = movieDao.getAllMovies()

            movies.ifEmpty {
                binding.textNoMovies.visibility = View.VISIBLE
                binding.recyclerViewAllMovies.visibility = View.GONE
            }

            if (movies.isNotEmpty()) {
                binding.textNoMovies.visibility = View.GONE
                binding.recyclerViewAllMovies.visibility = View.VISIBLE
                movieAdapter.updateMovies(movies)
            }
        }
    }

    private fun navigateToMovieDetailsFragment(movie: Movie) {
        val movieFragment = MovieFragment()
        val args = Bundle()
        args.putLong("movieId", movie.id)
        movieFragment.arguments = args

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, movieFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun deleteMovieFromDatabase(movie: Movie) {
        lifecycleScope.launch(Dispatchers.IO) {
            movieDao.deleteMovie(movie)
            val updatedMovies = movieDao.getAllMovies()
            launch(Dispatchers.Main) {
                movieAdapter.updateMovies(updatedMovies)
            }
        }
    }
}

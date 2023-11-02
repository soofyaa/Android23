package com.itis.android23.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itis.android23.R
import com.itis.android23.adapter.NewsAdapter
import com.itis.android23.databinding.FragmentFeedBinding
import com.itis.android23.model.News
import com.itis.android23.utils.NewsRepository

class FeedFragment : Fragment(R.layout.fragment_feed) {
    private var binding: FragmentFeedBinding? = null
    private val selectedFavorites = mutableSetOf<Int>()
    private val adapter = NewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFeedBinding.bind(view)
        val countNews = arguments?.getInt("countNews", 0) ?: 0

        if (countNews == 0) {
            showNoNews()
        } else {
            val items = NewsRepository.getNewsList(countNews)
            setupAdapter(items, savedInstanceState) // Удаление возврата адаптера
            setupLayoutManager(countNews)
            setupRecyclerView(adapter) // Использование исходного адаптера
        }
        binding?.btnOpenBottomSheet?.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        val editText = bottomSheetView.findViewById<EditText>(R.id.editText)
        val button = bottomSheetView.findViewById<Button>(R.id.btnSomeAction)

        // Создайте BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)

        button.setOnClickListener {
            val inputText = editText.text.toString()
            val countToAdd = inputText.toIntOrNull() ?: 0 // Проверка на ввод числа

            // Создайте новые элементы News и добавьте их в адаптер
            if (countToAdd > 0) {
                val itemsToAdd = NewsRepository.getNewsList(countToAdd)
                adapter.addNews(itemsToAdd)
            }

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showNoNews() {
        binding?.tvNoNews?.visibility = View.VISIBLE
    }

    private fun setupAdapter(items: List<News>, savedInstanceState: Bundle?) : NewsAdapter {
        adapter.newsList = items as ArrayList<News>

        val selectedFavorites = savedInstanceState?.getIntegerArrayList(SELECTED_NEWS_KEY)
        selectedFavorites?.let {
            this.selectedFavorites.addAll(it)
        }

        adapter.onFavoriteClick = { position ->
            toggleFavorite(position, items, adapter)
        }

        for (position in this.selectedFavorites) {
            items[position].isFavorite = true
        }

        adapter.onItemClick = { position ->
            navigateToInfoFragment(items[position])
        }

        return adapter
    }

    private fun toggleFavorite(position: Int, items: List<News>, adapter: NewsAdapter) {
        val isFavorite = items[position].isFavorite
        items[position].isFavorite = !isFavorite
        adapter.notifyItemChanged(position)

        if (!isFavorite) {
            selectedFavorites.add(position)
        } else {
            selectedFavorites.remove(position)
        }
    }

    private fun navigateToInfoFragment(newsItem: News) {
        val infoFragment = InfoFragment()

        val bundle = Bundle()
        bundle.putString("title", newsItem.title)
        infoFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, infoFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupLayoutManager(countNews: Int) {
        val layoutManager = when {
            countNews <= 12 -> LinearLayoutManager(requireContext())
            else -> GridLayoutManager(requireContext(), 2)
        }

        binding?.tvNoNews?.visibility = View.GONE
        binding?.recyclerView?.layoutManager = layoutManager
    }

    private fun setupRecyclerView(adapter: NewsAdapter) {
        binding?.recyclerView?.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val selectedFavoritesList = selectedFavorites.toList()
        outState.putIntegerArrayList(SELECTED_NEWS_KEY, ArrayList(selectedFavoritesList))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val SELECTED_NEWS_KEY = "selected_news_key"
    }
}

package com.itis.android23.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.itis.android23.R
import com.itis.android23.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private var binding: FragmentStartBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentStartBinding.bind(view)
        binding?.btnStart?.setOnClickListener {
            val inputText = binding?.etStart?.text.toString()
            val countNews: Int = if (inputText.isNotEmpty()) {
                inputText.toIntOrNull() ?: 0
            } else {
                0
            }
            navigateToNextScreen(countNews)
        }
    }

    private fun navigateToNextScreen(countNews: Int) {
        val feedFragment = FeedFragment()
        val args = Bundle().apply {
            putInt("countNews", countNews)
            putString("layoutManagerType", if (countNews > 12) "Grid" else "Linear")
        }
        feedFragment.arguments = args

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, feedFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
package com.itis.android23.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.itis.android23.R
import com.itis.android23.databinding.FragmentInfoBinding
import com.itis.android23.utils.NewsRepository


class InfoFragment : Fragment(R.layout.fragment_info) {
    private var binding: FragmentInfoBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInfoBinding.bind(view)
        val title = arguments?.getString("title").toString()

        val item = NewsRepository.findNewsByTitle(title)
        if (item != null) {
            binding?.run {
                tvTitle.text = item.title
                ivCover.setImageResource(item.imageId)
                tvDescription.text = item.description
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
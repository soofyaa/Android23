package com.itis.android23.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.itis.android23.R
import com.itis.android23.base.BaseFragment
import com.itis.android23.databinding.FragmentThirdBinding
import com.itis.android23.utils.ParamsKey

class ThirdFragment : BaseFragment(R.layout.fragment_third) {

    private var _binding: FragmentThirdBinding? = null
    private val binding: FragmentThirdBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentThirdBinding.bind(view)

        arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY)?.let { message ->
            binding.tv.text =
                if (message.isNotEmpty()) message else getString(R.string.third_fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val THIRD_FRAGMENT_TAG = "THIRD_FRAGMENT_TAG"
        fun newInstance(message: String) = ThirdFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }
}
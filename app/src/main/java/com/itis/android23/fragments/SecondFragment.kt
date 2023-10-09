package com.itis.android23.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.itis.android23.R
import com.itis.android23.base.BaseActivity
import com.itis.android23.base.BaseFragment
import com.itis.android23.databinding.FragmentSecondBinding
import com.itis.android23.utils.ActionType
import com.itis.android23.utils.ParamsKey

class SecondFragment : BaseFragment(R.layout.fragment_second) {

    private var _binding: FragmentSecondBinding? = null
    private val binding: FragmentSecondBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSecondBinding.bind(view)

        arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY)?.let { message ->
            binding.tv.text =
                if (message.isNotEmpty()) message else getString(R.string.second_fragment)
            binding.btnForward.setOnClickListener {

                parentFragmentManager.popBackStack()

                (requireActivity() as? BaseActivity)?.goToScreen(
                    actionType = ActionType.REPLACE,
                    destination = ThirdFragment.newInstance(message = message),
                    tag = ThirdFragment.THIRD_FRAGMENT_TAG,
                    isAddToBackStack = true
                )
            }
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SECOND_FRAGMENT_TAG = "SECOND_FRAGMENT_TAG"

        fun newInstance(message: String) = SecondFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }
}
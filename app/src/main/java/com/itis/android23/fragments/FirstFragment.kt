package com.itis.android23.fragments

import android.os.Bundle
import android.view.View
import com.itis.android23.R
import com.itis.android23.base.BaseActivity
import com.itis.android23.base.BaseFragment
import com.itis.android23.databinding.FragmentFirstBinding
import com.itis.android23.utils.ActionType

class FirstFragment : BaseFragment(R.layout.fragment_first) {

    private var _binding: FragmentFirstBinding? = null
    private val binding: FragmentFirstBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFirstBinding.bind(view)

        binding.btnFirst.setOnClickListener {
            val inputText = binding.etFirst.text.toString()

            (requireActivity() as? BaseActivity)?.goToScreen(
                actionType = ActionType.REPLACE,
                destination = SecondFragment.newInstance(message = inputText),
                tag = SecondFragment.SECOND_FRAGMENT_TAG,
                isAddToBackStack = true,
            )

            (requireActivity() as? BaseActivity)?.goToScreen(
                actionType = ActionType.REPLACE,
                destination = ThirdFragment.newInstance(message = inputText),
                tag = ThirdFragment.THIRD_FRAGMENT_TAG,
                isAddToBackStack = true
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT_TAG"
    }
}
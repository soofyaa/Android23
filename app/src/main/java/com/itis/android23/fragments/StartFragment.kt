package com.itis.android23.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.itis.android23.R
import com.itis.android23.databinding.FragmentStartBinding
import kotlin.math.min

class StartFragment : Fragment(R.layout.fragment_start) {

    private var binding: FragmentStartBinding? = null
    private var isPhoneValid = false
    private var isQuestionCountValid = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentStartBinding.bind(view)
        setUpPhoneEditText()
        setUpQuestionCountEditText()
        setUpStartButton()
    }

    private fun setUpPhoneEditText() {
        binding?.etPhone?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                formatPhoneText()
                isPhoneValid = isValidPhoneNumber(s.toString())
                updateStartButtonAvailability()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun formatPhoneText() {

        val phoneNumber = binding?.etPhone?.text.toString()

        if (phoneNumber.length > 18) {
            Toast.makeText(this.context, R.string.extra_numbers, Toast.LENGTH_SHORT).show()
            return
        }

        if (isPhoneNeedFormat(phoneNumber) ) {
            val formattedPhone = formatPhone(phoneNumber)
            binding?.etPhone?.setText(formattedPhone)
            binding?.etPhone?.setSelection(formattedPhone.length)
        }
    }

    private fun isPhoneNeedFormat(phone: String): Boolean {
        val regex = Regex(getString(R.string.regex_is_phone_need_format))
        return !phone.matches(regex)
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val regex = Regex(getString(R.string.regex_is_valid_phone_number))
        return phone.matches(regex)
    }

    private fun formatPhone(phone: String): String {
        val digits = phone.replace("\\D".toRegex(), "")

        val formattedPhone = StringBuilder()

        if (digits.isEmpty()) {
            return ""
        }

        formattedPhone.append("+7 (9")

        if (digits.length < 3) {
            return formattedPhone.toString()
        } else if (digits.length == 3) {
            formattedPhone.append(digits[2])
            return formattedPhone.toString()
        } else {
            formattedPhone.append(digits.substring(2, 4)).append(")-")
        }

        if (digits.length == 4) {
            return formattedPhone.toString()
        } else {
            formattedPhone.append(digits.substring(4, min(7, digits.length)))
        }

        if (digits.length < 7) {
            return formattedPhone.toString()
        } else {
            formattedPhone.append("-")
        }

        if (digits.length == 7) {
            return formattedPhone.toString()
        } else {
            formattedPhone.append(digits.substring(7, min(9, digits.length)))
        }

        if (digits.length < 9) {
            return formattedPhone.toString()
        } else {
            formattedPhone.append("-")
        }

        if (digits.length == 9) {
            return formattedPhone.toString()
        } else {
            formattedPhone.append(digits.substring(9, digits.length))
        }
        return formattedPhone.toString()
    }

    private fun setUpQuestionCountEditText() {
        binding?.etQuestionsCount?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val questionCount = s?.toString()?.toIntOrNull() ?: 0
                isQuestionCountValid = questionCount in 1..10

                if (!isQuestionCountValid) {
                    Toast.makeText(context,
                        getString(R.string.valid_questions_count), Toast.LENGTH_SHORT).show()
                }

                updateStartButtonAvailability()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun updateStartButtonAvailability() {
        binding?.btnStart?.isEnabled = isPhoneValid && isQuestionCountValid
    }

    private fun setUpStartButton() {
        binding?.btnStart?.setOnClickListener {
            val questionCount = binding?.etQuestionsCount?.text.toString().toInt()
            val viewPagerFragment = ViewPagerFragment.newInstance(questionCount)
            parentFragmentManager
                .beginTransaction()
                .addToBackStack("StartFragment")
                .replace(R.id.fragment_container, viewPagerFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val START_FRAGMENT_TAG = "START_FRAGMENT_TAG"
        fun getInstance() : StartFragment {
            return StartFragment()
        }
    }
}
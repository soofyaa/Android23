package com.itis.android23.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itis.android23.R
import com.itis.android23.db.AppDatabase
import com.itis.android23.fragments.LoginFragment
import com.itis.android23.utils.OnPhoneChangeListener
import kotlinx.coroutines.launch

class ChangePhoneBottomSheet : BottomSheetDialogFragment() {

    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    var onPhoneChangeListener: OnPhoneChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences
        if (context is OnPhoneChangeListener) {
            onPhoneChangeListener = context
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_change_phone, container, false)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val etNewPhone = view.findViewById<EditText>(R.id.etNewPhone)

        btnSave.setOnClickListener {
            val newPhone = etNewPhone.text.toString()

            lifecycleScope.launch {
                val userDao = AppDatabase.getInstance(requireContext()).userDao()

                val phoneCount = userDao.getCountPhone(newPhone)
                if (phoneCount == 0) {
                    val userId = getUserId()
                    userDao.updatePhoneNumber(userId, newPhone)

                    showToast("Phone changed successfully")

                    onPhoneChangeListener?.onPhoneChanged(newPhone)

                    dismiss()
                } else {
                    showToast("Phone number is already taken")
                }
            }
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getUserId(): Long {
        return sharedPreferences.getLong(LoginFragment.KEY_USER_ID, -1)
    }
}

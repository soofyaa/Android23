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
import kotlinx.coroutines.launch

class ChangePasswordBottomSheet : BottomSheetDialogFragment() {

    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_change_password, container, false)

        val btnSavePassword = view.findViewById<Button>(R.id.btnSave)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)

        btnSavePassword.setOnClickListener {
            val newPassword = etNewPassword.text.toString()

            lifecycleScope.launch {
                val userDao = AppDatabase.getInstance(requireContext()).userDao()
                val userId = getUserId()
                userDao.updatePassword(userId, newPassword)

                showToast("Password changed successfully")

                dismiss()
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

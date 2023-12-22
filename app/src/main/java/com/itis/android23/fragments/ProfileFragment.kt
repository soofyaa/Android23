package com.itis.android23.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.android23.R
import com.itis.android23.bottomsheet.ChangePasswordBottomSheet
import com.itis.android23.bottomsheet.ChangePhoneBottomSheet
import com.itis.android23.databinding.FragmentProfileBinding
import com.itis.android23.db.AppDatabase
import com.itis.android23.utils.OnPhoneChangeListener

class ProfileFragment : Fragment(), OnPhoneChangeListener {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val userId = getUserId()

        binding.root.post {
            lifecycleScope.launchWhenResumed {
                val userDao = AppDatabase.getInstance(requireContext()).userDao()
                val user = userDao.getUserById(userId)

                val phone = getString(R.string.your_phone, user?.phone)
                val email = getString(R.string.your_email, user?.email)
                val name = getString(R.string.your_name, user?.name)

                binding.textViewPhone.text = phone
                binding.textViewEmail.text = email
                binding.textViewName.text = name
            }
        }

        binding.btnChangePhone.setOnClickListener {
            openBottomSheetForPhoneChange()
        }

        binding.btnChangePassword.setOnClickListener {
            openBottomSheetForPasswordChange()
        }

        binding.btnDeleteAccount.setOnClickListener {
            showConfirmationDialog()
        }

        binding.btnLogout.setOnClickListener {
            sharedPreferences.edit().remove(LoginFragment.KEY_USER_ID).apply()
            sharedPreferences.edit().putBoolean(LoginFragment.KEY_IS_LOGGED_IN, false).apply()
            navigateToLoginScreen()
            Toast.makeText(requireActivity(),
                getString(R.string.you_have_logged_out_of_your_account), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_account))
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_your_account_this_action_cannot_be_undone))

        builder.setPositiveButton(getString(R.string.delete)) { dialog, which ->
            deleteProfile()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun deleteProfile() {
        val userId = getUserId()

        lifecycleScope.launchWhenResumed {
            val userDao = AppDatabase.getInstance(requireContext()).userDao()

            userDao.deleteUserById(userId)

            sharedPreferences.edit().remove(LoginFragment.KEY_USER_ID).apply()
            sharedPreferences.edit().putBoolean(LoginFragment.KEY_IS_LOGGED_IN, false).apply()

            navigateToLoginScreen()
        }
    }

    private fun navigateToLoginScreen() {
        val loginFragment = LoginFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, loginFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openBottomSheetForPhoneChange() {
        val bottomSheetFragment = ChangePhoneBottomSheet()
        bottomSheetFragment.onPhoneChangeListener = this
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }

    private fun openBottomSheetForPasswordChange() {
        val bottomSheetFragment = ChangePasswordBottomSheet()
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }

    private fun getUserId(): Long {
        return sharedPreferences.getLong(LoginFragment.KEY_USER_ID, -1)
    }

    override fun onPhoneChanged(newPhone: String) {
        binding.textViewPhone.text = getString(R.string.your_phone, newPhone)
    }
}

package com.itis.android23.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.android23.R
import com.itis.android23.data.User
import com.itis.android23.databinding.FragmentRegisterBinding
import com.itis.android23.db.AppDatabase
import com.itis.android23.db.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val userDao: UserDao by lazy {
        AppDatabase.getInstance(requireContext()).userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRegisterBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.editTextNameRegister.text.toString()
            val phone = binding.editTextPhoneRegister.text.toString()
            val email = binding.editTextEmailRegister.text.toString()
            val password = binding.editTextPasswordRegister.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                val existingUserByPhone = userDao.getUserByPhone(phone)

                if (existingUserByPhone == null) {
                    val existingUserByEmail = userDao.getUserByEmail(email)

                    if (existingUserByEmail == null) {
                        userDao.insertUser(User(0, name, phone, email, password))

                        withContext(Dispatchers.Main) {
                            showToast(getString(R.string.user_added_to_the_database))
                            activity?.onBackPressed()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            showToast(getString(R.string.user_with_this_email_already_exists))
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast(getString(R.string.user_with_this_phone_already_exists))
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

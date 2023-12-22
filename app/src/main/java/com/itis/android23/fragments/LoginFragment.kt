package com.itis.android23.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.android23.R
import com.itis.android23.data.User
import com.itis.android23.databinding.FragmentLoginBinding
import com.itis.android23.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val userDao by lazy { AppDatabase.getInstance(requireContext()).userDao() }
    private val sharedPreferences by lazy {
        requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        if (isLoggedIn) {
            navigateToMainScreen()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmailLogin.text.toString()
            val password = binding.editTextPasswordLogin.text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                val user = getUserFromDatabase(email, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        val userId = getUserIdFromDatabase(email, password)
                        saveUserId(userId)
                        saveLoginState(true)
                        navigateToMainScreen()
                    } else {
                        showToast(getString(R.string.user_not_found))
                    }
                }
            }
        }

        binding.btnGoToRegister.setOnClickListener {
            navigateToRegisterFragment()
        }
    }

    private suspend fun getUserFromDatabase(email: String, password: String): User? {
        return userDao.getUserByEmailAndPassword(email, password)
    }

    private fun navigateToMainScreen() {
        val mainFragment = MainFragment()

        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mainFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToRegisterFragment() {
        val registerFragment = RegisterFragment()

        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, registerFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun saveUserId(userId: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(KEY_USER_ID, userId)
        editor.apply()
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    private suspend fun getUserIdFromDatabase(email: String, password: String): Long {
        val user = userDao.getUserByEmailAndPassword(email, password)
        return user?.id ?: -1
    }

    companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
}

package com.itis.android23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.android23.databinding.ActivityMainBinding
import com.itis.android23.fragments.AddMovieFragment
import com.itis.android23.fragments.LoginFragment
import com.itis.android23.fragments.MainFragment
import com.itis.android23.fragments.ProfileFragment


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }


        findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.let {
            it.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_main -> {
                        replaceFragment(MainFragment())
                        true
                    }

                    R.id.navigation_add_movie -> {
                        replaceFragment(AddMovieFragment())
                        true
                    }

                    R.id.navigation_profile -> {
                        replaceFragment(ProfileFragment())
                        true
                    }

                    else -> false
                }
            }

            replaceFragment(LoginFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment, fragment.javaClass.name)
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
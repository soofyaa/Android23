package com.itis.android23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itis.android23.databinding.ActivityMainBinding
import com.itis.android23.fragments.StartFragment


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var binding : ActivityMainBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.let {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,
                    StartFragment.getInstance(),
                    StartFragment.START_FRAGMENT_TAG)
                .commit()
        }
    }
}
package com.itis.android23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itis.android23.fragments.StartFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, StartFragment())
            .commit()
    }
}
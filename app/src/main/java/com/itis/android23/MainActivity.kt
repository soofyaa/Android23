package com.itis.android23

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.android23.fragments.CoroutinesFragment
import com.itis.android23.fragments.MainFragment
import com.itis.android23.fragments.SettingsFragment
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private val notificationPermissionCode = 123
    private var notificationPermissionDeniedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onNewIntent(intent)

        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkNotificationPermission()
        }

        findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.apply {
            setOnNavigationItemSelectedListener { menuItem ->
                handleBottomNavigation(menuItem.itemId)
            }
        }
        replaceFragment(MainFragment())
    }

    private fun handleBottomNavigation(itemId: Int): Boolean {
        when (itemId) {
            R.id.action_main -> replaceFragment(MainFragment())
            R.id.action_notification_settings -> replaceFragment(SettingsFragment())
            R.id.action_coroutines -> replaceFragment(CoroutinesFragment())
            else -> return false
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment, fragment.javaClass.name)
            addToBackStack(null)
            commit()
        }
    }

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionDeniedCount.takeIf { it < 2 }?.run {
                requestNotificationPermission()
            } ?: showPermissionDeniedMessage()
        }
    }

    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            notificationPermissionCode
        )
    }

    private fun showPermissionDeniedMessage() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.notifications_are_disabled))
            setMessage(getString(R.string.please_enable_notifications_in_the_application_settings))
            setPositiveButton(getString(R.string.settings)) { _, _ -> openAppSettings() }
            setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
            show()
        }
    }

    private fun openAppSettings() {
        val settingsIntent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
        startActivity(settingsIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == notificationPermissionCode && grantResults.isNotEmpty() &&
            grantResults[0] != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionDeniedCount++
            checkNotificationPermission()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.getStringExtra(getString(R.string.open_fragment)) == getString(R.string.main_extra)) {
            replaceFragment(MainFragment())
            val text = getString(R.string.from_notification_to_mainfragment)
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        } else if (intent?.getStringExtra(getString(R.string.open_fragment)) == getString(R.string.settings_extra)){
           replaceFragment(SettingsFragment())
        }
    }
}

package com.itis.android23.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.itis.android23.MainActivity
import com.itis.android23.R
import com.itis.android23.data.NotificationConstants
import com.itis.android23.data.NotificationSettings
import com.itis.android23.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _viewBinding: FragmentMainBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val notificationChannelId = NotificationConstants.CHANNEL_ID
    private val notificationChannelName = NotificationConstants.CHANNEL_NAME
    private val notificationId = NotificationConstants.NOTIFICATION_ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentMainBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.showNotificationButton.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {
        val title = viewBinding.titleEditText.text.toString()
        val message = viewBinding.messageEditText.text.toString()

        val intent = Intent(requireContext(), MainActivity::class.java)
            .apply {
                putExtra(getString(R.string.open_fragment), getString(R.string.main_extra))
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

        val openAppPendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val settingsIntent = Intent(requireContext(), MainActivity::class.java)
            .apply {
                putExtra(getString(R.string.open_fragment), getString(R.string.settings_extra))
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

        val openSettingsPendingIntent = PendingIntent.getActivity(
            requireContext(),
            1,
            settingsIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        var notificationBuilder = NotificationCompat.Builder(requireContext(), notificationChannelId)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationSettings.importanceCheckedId)
            .setVisibility(NotificationSettings.privacyCheckedId)
            .setContentTitle(title)
            .setContentText(message)

        if (NotificationSettings.isDetailedTextChecked) {
            notificationBuilder = notificationBuilder
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        }

        if (NotificationSettings.isShowButtonsChecked) {
            notificationBuilder = notificationBuilder
                .addAction(R.drawable.ic_home, getString(R.string.main), openAppPendingIntent)
                .addAction(R.drawable.ic_notifications, getString(R.string.settings), openSettingsPendingIntent)
        }

        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}

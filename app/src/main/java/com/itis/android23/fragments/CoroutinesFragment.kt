package com.itis.android23.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.itis.android23.R
import com.itis.android23.data.CoroutinesSettings
import com.itis.android23.data.NotificationConstants
import com.itis.android23.databinding.FragmentCoroutinesBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class CoroutinesFragment : Fragment(R.layout.fragment_coroutines) {

    private var binding: FragmentCoroutinesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoroutinesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    CoroutinesSettings.count = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            checkBoxAsync.setOnCheckedChangeListener { _, isChecked ->
                CoroutinesSettings.async = isChecked
            }

            checkBoxStopOnBackground.setOnCheckedChangeListener { _, isChecked ->
                CoroutinesSettings.stopOnBackground = isChecked
            }

            buttonExecute.setOnClickListener {
                startCoroutines()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startCoroutines() {
        job = lifecycleScope.launch(IO) {
            repeat(CoroutinesSettings.count) {
                if (CoroutinesSettings.async) {
                    launch { startCoroutine(it + 1) }
                } else {
                    startCoroutine(it + 1)
                }
            }

            withContext(Dispatchers.Main) {
                sendCoroutinesFinishedNotification(requireContext())
            }

            job = null
        }
    }

    private suspend fun startCoroutine(index: Int) {
        delay(1000)
        Log.e(javaClass.name, "Coroutine $index finished.")
    }

    override fun onStop() {
        super.onStop()
        if (CoroutinesSettings.stopOnBackground) {
            job?.cancel(getString(R.string.coroutines_stop))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendCoroutinesFinishedNotification(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationConstants.CHANNEL_ID,
                NotificationConstants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_coroutines)
            .setContentTitle(getString(R.string.coroutines_finished))
            .setContentText(getString(R.string.my_job_here_is_done))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NotificationConstants.NOTIFICATION_ID, notification)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.coroutineContext.cancelChildren()
    }

    companion object {
        private var job: Job? = null
    }
}

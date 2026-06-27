package com.choque.authcares2.features.alerts.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat

class EmergencyAlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra(CriticalAlertNotifier.EXTRA_TITLE)
            ?: "¡Alerta crítica!"
        val message = intent?.getStringExtra(CriticalAlertNotifier.EXTRA_MESSAGE)
            ?: "Revisa al niño inmediatamente."

        val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        } else {
            0
        }

        ServiceCompat.startForeground(
            this,
            CriticalAlertNotifier.NOTIFICATION_ID,
            CriticalAlertNotifier.buildNotification(this, title, message),
            serviceType
        )
        startAlarmHardware()
        return START_NOT_STICKY
    }

    private fun startAlarmHardware() {
        if (mediaPlayer?.isPlaying != true) {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(this@EmergencyAlarmService, alarmUri)
                isLooping = true
                prepare()
                start()
            }
        }

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createWaveform(CriticalAlertNotifier.vibrationPattern, 0)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(CriticalAlertNotifier.vibrationPattern, 0)
        }
    }

    private fun stopAlarmHardware() {
        mediaPlayer?.runCatching {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
        vibrator?.cancel()
        vibrator = null
    }

    override fun onDestroy() {
        stopAlarmHardware()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    companion object {
        fun start(context: Context, title: String, message: String) {
            if (!CriticalAlertNotifier.hasNotificationPermission(context)) return

            val intent = Intent(context, EmergencyAlarmService::class.java).apply {
                putExtra(CriticalAlertNotifier.EXTRA_TITLE, title)
                putExtra(CriticalAlertNotifier.EXTRA_MESSAGE, message)
            }

            try {
                ContextCompat.startForegroundService(context, intent)
            } catch (_: RuntimeException) {
                CriticalAlertNotifier.showCriticalAlert(context, title, message)
            }
        }
    }
}

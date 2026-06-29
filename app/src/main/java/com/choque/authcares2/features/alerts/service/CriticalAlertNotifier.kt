package com.choque.authcares2.features.alerts.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.choque.authcares2.R

object CriticalAlertNotifier {
    const val CHANNEL_ID = "critical_emergency_alerts_v1"
    const val NOTIFICATION_ID = 911
    const val EXTRA_TITLE = "critical_alert_title"
    const val EXTRA_MESSAGE = "critical_alert_message"

    val vibrationPattern = longArrayOf(0, 900, 250, 900, 250, 1400, 350)

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Alertas críticas de emergencia",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Crisis de ansiedad o pánico que requieren atención inmediata"
            enableVibration(true)
            vibrationPattern = CriticalAlertNotifier.vibrationPattern
            enableLights(true)
            lightColor = Color.RED
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(alarmSound, audioAttributes)
        }

        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun buildNotification(context: Context, title: String, message: String): Notification {
        createNotificationChannel(context)

        val fullScreenIntent = Intent(context, EmergencyAlertActivity::class.java).apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, message)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_authcares_notifications_active)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSound(alarmSound)
            .setVibrate(vibrationPattern)
            .setContentIntent(fullScreenPendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()
    }

    fun showCriticalAlert(
        context: Context,
        title: String = "¡Alerta crítica!",
        message: String = "Se detectó una posible crisis. Revisa al niño inmediatamente."
    ) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        try {
            NotificationManagerCompat.from(context).notify(
                NOTIFICATION_ID,
                buildNotification(context, title, message)
            )
        } catch (_: SecurityException) {
            // El permiso puede cambiar antes de mostrar la alerta.
        }
    }

    fun dismiss(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }
}

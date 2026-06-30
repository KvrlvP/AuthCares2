package com.choque.authcares2.features.alerts.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.choque.authcares2.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.sqrt

class SensorMonitoringService : Service() {

    private var sensorReference: DatabaseReference? = null
    private var sensorListener: ValueEventListener? = null

    private var lastHeartRateAlertAt = 0L
    private var lastMovementAlertAt = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        val watchId = intent?.getStringExtra(EXTRA_WATCH_ID)

        if (watchId.isNullOrBlank()) {
            stopSelf()
            return START_NOT_STICKY
        }

        startAsForeground()
        startFirebaseListener(watchId)

        return START_STICKY
    }

    private fun startAsForeground() {
        createMonitoringChannel()

        val notification = NotificationCompat.Builder(
            this,
            MONITORING_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_authcares_notifications_active)
            .setContentTitle("AuthCares está monitoreando")
            .setContentText("Supervisando el ritmo cardíaco y movimiento del reloj.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true)
            .setSilent(true)
            .build()

        val serviceType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }

        ServiceCompat.startForeground(
            this,
            MONITORING_NOTIFICATION_ID,
            notification,
            serviceType
        )
    }

    private fun startFirebaseListener(watchId: String) {
        removeFirebaseListener()

        val reference = FirebaseDatabase.getInstance()
            .getReference("pending_wearables")
            .child(watchId)
            .child("latest")

        sensorReference = reference

        sensorListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) return

                val heartRate =
                    snapshot.child("hr").getValue(Int::class.java)
                        ?: snapshot.child("hr")
                            .getValue(Long::class.java)
                            ?.toInt()

                val accX = snapshot.child("acc")
                    .child("x")
                    .getValue(Double::class.java) ?: 0.0

                val accY = snapshot.child("acc")
                    .child("y")
                    .getValue(Double::class.java) ?: 0.0

                val accZ = snapshot.child("acc")
                    .child("z")
                    .getValue(Double::class.java) ?: 0.0

                evaluateHeartRate(heartRate)
                evaluateMovement(accX, accY, accZ)
            }

            override fun onCancelled(error: DatabaseError) {
                // Puedes agregar Log.e aquí para depuración.
            }
        }

        reference.addValueEventListener(sensorListener!!)
    }

    private fun evaluateHeartRate(heartRate: Int?) {
        if (heartRate == null) return

        val now = System.currentTimeMillis()

        if (
            heartRate >= HEART_RATE_LIMIT &&
            now - lastHeartRateAlertAt >= ALERT_COOLDOWN
        ) {
            lastHeartRateAlertAt = now

            EmergencyAlarmService.start(
                context = this,
                title = "Ritmo cardíaco elevado",
                message = "Se detectaron $heartRate latidos por minuto."
            )
        }
    }

    private fun evaluateMovement(
        x: Double,
        y: Double,
        z: Double
    ) {
        val magnitude = sqrt(x * x + y * y + z * z)
        val now = System.currentTimeMillis()

        if (
            magnitude >= MOVEMENT_LIMIT &&
            now - lastMovementAlertAt >= ALERT_COOLDOWN
        ) {
            lastMovementAlertAt = now

            EmergencyAlarmService.start(
                context = this,
                title = "Movimiento brusco detectado",
                message = "El reloj detectó un movimiento inusual."
            )
        }
    }

    private fun removeFirebaseListener() {
        val reference = sensorReference
        val listener = sensorListener

        if (reference != null && listener != null) {
            reference.removeEventListener(listener)
        }

        sensorReference = null
        sensorListener = null
    }

    private fun createMonitoringChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            MONITORING_CHANNEL_ID,
            "Monitoreo continuo",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Indica que AuthCares está monitoreando el smartwatch."
            setSound(null, null)
            enableVibration(false)
        }

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    override fun onDestroy() {
        removeFirebaseListener()

        ServiceCompat.stopForeground(
            this,
            ServiceCompat.STOP_FOREGROUND_REMOVE
        )

        super.onDestroy()
    }

    companion object {
        private const val EXTRA_WATCH_ID = "monitoring_watch_id"

        private const val MONITORING_CHANNEL_ID =
            "sensor_monitoring_channel"

        private const val MONITORING_NOTIFICATION_ID = 912

        private const val HEART_RATE_LIMIT = 120
        private const val MOVEMENT_LIMIT = 15.0

        private const val ALERT_COOLDOWN =
            5L * 60L * 1000L

        fun start(context: Context, watchId: String) {
            val intent = Intent(
                context,
                SensorMonitoringService::class.java
            ).apply {
                putExtra(EXTRA_WATCH_ID, watchId)
            }

            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            context.stopService(
                Intent(context, SensorMonitoringService::class.java)
            )
        }
    }
}
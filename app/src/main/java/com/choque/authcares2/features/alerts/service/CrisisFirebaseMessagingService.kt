package com.choque.authcares2.features.alerts.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CrisisFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val crisisFlag = data["crisis"]?.trim()?.lowercase()
        val isCrisis = crisisFlag in setOf("true", "1", "yes", "si", "sí")

        if (!isCrisis) return

        val childName = data["child_name"]?.trim().orEmpty()
        val title = data["title"]?.takeIf { it.isNotBlank() }
            ?: "¡Alerta crítica de emergencia!"
        val defaultMessage = if (childName.isBlank()) {
            "Se detectó una posible crisis. Revisa al niño inmediatamente."
        } else {
            "Se detectó una posible crisis en $childName. Revísalo inmediatamente."
        }
        val alertMessage = data["message"]?.takeIf { it.isNotBlank() } ?: defaultMessage

        EmergencyAlarmService.start(
            context = this,
            title = title,
            message = alertMessage
        )
    }
}

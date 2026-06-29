package com.choque.authcares2.features.alerts.model

enum class AlertType {
    HEART_RATE,
    INTENSE_MOVEMENT,
    COMBINED
}

data class AlertItem(
    val id: String,
    val childName: String,
    val type: AlertType,
    val title: String,
    val description: String,
    val startedAt: Long,
    val endedAt: Long,
    val maximumHeartRate: Int?,
    val maximumMovement: Double?,
    val measurementCount: Int
)
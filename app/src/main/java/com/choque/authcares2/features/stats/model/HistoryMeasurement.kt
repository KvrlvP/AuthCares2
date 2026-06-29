package com.choque.authcares2.features.stats.model

data class SensorVector(
    val x: Double,
    val y: Double,
    val z: Double
)

data class HistoryMeasurement(
    val heartRate: Int?,
    val acceleration: SensorVector?,
    val gyroscope: SensorVector?,
    val timestamp: Long
)

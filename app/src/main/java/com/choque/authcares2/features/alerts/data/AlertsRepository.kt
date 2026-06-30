package com.choque.authcares2.features.alerts.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class AlertSensorSample(
    val timestamp: Long,
    val heartRate: Int?,
    val accelerationX: Double?,
    val accelerationY: Double?,
    val accelerationZ: Double?
)

interface AlertsRepository {
    fun loadHistory(watchId: String): Flow<Result<List<AlertSensorSample>>>
}

class FirebaseAlertsRepository(
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) : AlertsRepository {

    override fun loadHistory(
        watchId: String
    ): Flow<Result<List<AlertSensorSample>>> = callbackFlow {

        val reference = database.getReference("pending_wearables")
            .child(watchId)
            .child("history")
            .limitToLast(100)

        reference.get()
            .addOnSuccessListener { snapshot ->
                val samples = snapshot.children
                    .mapNotNull(::toSample)
                    .sortedBy { it.timestamp }

                trySend(Result.success(samples))
                close()
            }
            .addOnFailureListener { error ->
                trySend(Result.failure(error))
                close()
            }

        awaitClose()
    }

    private fun toSample(snapshot: DataSnapshot): AlertSensorSample? {
        val rawTimestamp = snapshot.child("ts").numberAsLong()
            ?: snapshot.key?.toLongOrNull()
            ?: return null
        val timestamp = if (rawTimestamp < SECONDS_LIMIT) {
            rawTimestamp * 1000L
        } else {
            rawTimestamp
        }
        val acceleration = snapshot.child("acc")

        return AlertSensorSample(
            timestamp = timestamp,
            heartRate = snapshot.child("hr").numberAsInt(),
            accelerationX = acceleration.child("x").numberAsDouble(),
            accelerationY = acceleration.child("y").numberAsDouble(),
            accelerationZ = acceleration.child("z").numberAsDouble()
        )
    }

    private fun DataSnapshot.numberAsInt(): Int? =
        getValue(Long::class.java)?.toInt()
            ?: getValue(Double::class.java)?.toInt()

    private fun DataSnapshot.numberAsLong(): Long? =
        getValue(Long::class.java)
            ?: getValue(Double::class.java)?.toLong()

    private fun DataSnapshot.numberAsDouble(): Double? =
        getValue(Double::class.java)
            ?: getValue(Long::class.java)?.toDouble()

    private companion object {
        const val SECONDS_LIMIT = 10_000_000_000L
    }
}

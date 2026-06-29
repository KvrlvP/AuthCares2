package com.choque.authcares2.features.alerts.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    fun observeHistory(watchId: String): Flow<Result<List<AlertSensorSample>>>
}

class FirebaseAlertsRepository(
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) : AlertsRepository {

    override fun observeHistory(
        watchId: String
    ): Flow<Result<List<AlertSensorSample>>> = callbackFlow {
        val reference = database.getReference("pending_wearables")
            .child(watchId)
            .child("history")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val samples = snapshot.children
                    .mapNotNull(::toSample)
                    .sortedBy { it.timestamp }
                trySend(Result.success(samples))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }

        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
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

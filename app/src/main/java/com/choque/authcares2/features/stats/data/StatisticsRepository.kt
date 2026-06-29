package com.choque.authcares2.features.stats.data

import com.choque.authcares2.features.stats.model.HistoryMeasurement
import com.choque.authcares2.features.stats.model.SensorVector
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface StatisticsRepository {
    fun observeHistory(watchId: String): Flow<Result<List<HistoryMeasurement>>>
}

class FirebaseStatisticsRepository(
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) : StatisticsRepository {

    override fun observeHistory(
        watchId: String
    ): Flow<Result<List<HistoryMeasurement>>> = callbackFlow {
        val reference = database.getReference("pending_wearables")
            .child(watchId)
            .child("history")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val measurements = snapshot.children.mapNotNull(::toMeasurement)
                    .sortedBy { it.timestamp }
                trySend(Result.success(measurements))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }

        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    private fun toMeasurement(snapshot: DataSnapshot): HistoryMeasurement? {
        val timestamp = snapshot.child("ts").numberAsLong()
            ?: snapshot.key?.toLongOrNull()
            ?: return null

        return HistoryMeasurement(
            heartRate = snapshot.child("hr").numberAsInt(),
            acceleration = snapshot.child("acc").toVector(),
            gyroscope = snapshot.child("gyr").toVector(),
            timestamp = timestamp
        )
    }

    private fun DataSnapshot.toVector(): SensorVector? {
        val x = child("x").numberAsDouble() ?: return null
        val y = child("y").numberAsDouble() ?: return null
        val z = child("z").numberAsDouble() ?: return null
        return SensorVector(x, y, z)
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
}

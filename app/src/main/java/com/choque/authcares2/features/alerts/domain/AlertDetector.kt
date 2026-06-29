package com.choque.authcares2.features.alerts.domain

import com.choque.authcares2.features.alerts.data.AlertSensorSample
import com.choque.authcares2.features.alerts.model.AlertItem
import com.choque.authcares2.features.alerts.model.AlertType
import kotlin.math.sqrt

class AlertDetector {

    fun detect(
        samples: List<AlertSensorSample>,
        childName: String
    ): List<AlertItem> {
        val alerts = mutableListOf<AlertItem>()
        var episode: Episode? = null

        fun finishEpisode() {
            episode?.toAlert(childName)?.let(alerts::add)
            episode = null
        }

        samples.sortedBy { it.timestamp }.forEach { sample ->
            val movement = movementMagnitude(sample)
            val type = classify(sample.heartRate, movement)

            if (type == null) {
                finishEpisode()
                return@forEach
            }

            val current = episode
            val continuesCurrentEpisode = current != null &&
                current.type == type &&
                sample.timestamp - current.endedAt <= MAX_EPISODE_GAP

            if (continuesCurrentEpisode) {
                episode = current?.add(sample, movement)
            } else {
                finishEpisode()
                episode = Episode.from(sample, type, movement)
            }
        }
        finishEpisode()

        return alerts
            .sortedByDescending { it.endedAt }
            .take(MAX_ALERTS)
    }

    private fun classify(heartRate: Int?, movement: Double?): AlertType? {
        val elevatedHeartRate = heartRate != null && heartRate >= HIGH_HEART_RATE
        val intenseMovement = movement != null && movement >= INTENSE_MOVEMENT

        return when {
            elevatedHeartRate && intenseMovement -> AlertType.COMBINED
            elevatedHeartRate -> AlertType.HEART_RATE
            intenseMovement -> AlertType.INTENSE_MOVEMENT
            else -> null
        }
    }

    private fun movementMagnitude(sample: AlertSensorSample): Double? {
        val x = sample.accelerationX ?: return null
        val y = sample.accelerationY ?: return null
        val z = sample.accelerationZ ?: return null
        return sqrt(x * x + y * y + z * z)
    }

    private data class Episode(
        val type: AlertType,
        val startedAt: Long,
        val endedAt: Long,
        val maximumHeartRate: Int?,
        val maximumMovement: Double?,
        val measurementCount: Int
    ) {
        fun add(sample: AlertSensorSample, movement: Double?): Episode = copy(
            endedAt = sample.timestamp,
            maximumHeartRate = listOfNotNull(maximumHeartRate, sample.heartRate).maxOrNull(),
            maximumMovement = listOfNotNull(maximumMovement, movement).maxOrNull(),
            measurementCount = measurementCount + 1
        )

        fun toAlert(childName: String): AlertItem {
            val title = when (type) {
                AlertType.HEART_RATE -> "Ritmo cardíaco elevado"
                AlertType.INTENSE_MOVEMENT -> "Movimiento intenso detectado"
                AlertType.COMBINED -> "Pulso y movimiento elevados"
            }
            val description = when (type) {
                AlertType.HEART_RATE ->
                    "El historial registró un ritmo cardíaco máximo de " +
                        "${maximumHeartRate ?: "--"} BPM."
                AlertType.INTENSE_MOVEMENT ->
                    "El acelerómetro registró un nivel de movimiento intenso."
                AlertType.COMBINED ->
                    "El historial registró un pulso máximo de " +
                        "${maximumHeartRate ?: "--"} BPM junto con movimiento intenso."
            }

            return AlertItem(
                id = "${type.name}-$startedAt",
                childName = childName,
                type = type,
                title = title,
                description = description,
                startedAt = startedAt,
                endedAt = endedAt,
                maximumHeartRate = maximumHeartRate,
                maximumMovement = maximumMovement,
                measurementCount = measurementCount
            )
        }

        companion object {
            fun from(
                sample: AlertSensorSample,
                type: AlertType,
                movement: Double?
            ) = Episode(
                type = type,
                startedAt = sample.timestamp,
                endedAt = sample.timestamp,
                maximumHeartRate = sample.heartRate,
                maximumMovement = movement,
                measurementCount = 1
            )
        }
    }

    private companion object {
        const val HIGH_HEART_RATE = 120
        const val INTENSE_MOVEMENT = 15.0
        const val MAX_EPISODE_GAP = 5L * 60L * 1000L
        const val MAX_ALERTS = 100
    }
}

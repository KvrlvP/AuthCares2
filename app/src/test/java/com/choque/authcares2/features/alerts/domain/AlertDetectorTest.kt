package com.choque.authcares2.features.alerts.domain

import com.choque.authcares2.features.alerts.data.AlertSensorSample
import com.choque.authcares2.features.alerts.model.AlertType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AlertDetectorTest {

    private val detector = AlertDetector()

    @Test
    fun detectaAlertaPorRitmoCardiacoAlto() {
        val alerts = detector.detect(
            samples = listOf(
                sample(timestamp = 1_000L, heartRate = 121)
            ),
            childName = "Bennett"
        )

        assertEquals(1, alerts.size)
        assertEquals(AlertType.HEART_RATE, alerts.first().type)
        assertEquals("Bennett", alerts.first().childName)
        assertEquals(121, alerts.first().maximumHeartRate)
    }

    @Test
    fun detectaAlertaPorMovimientoIntenso() {
        val alerts = detector.detect(
            samples = listOf(
                sample(timestamp = 1_000L, heartRate = 80, x = 15.0, y = 0.0, z = 0.0)
            ),
            childName = "Bennett"
        )

        assertEquals(1, alerts.size)
        assertEquals(AlertType.INTENSE_MOVEMENT, alerts.first().type)
    }

    @Test
    fun noDetectaAlertasCuandoLosDatosSonNormales() {
        val alerts = detector.detect(
            samples = listOf(
                sample(timestamp = 1_000L, heartRate = 80, x = 1.0, y = 1.0, z = 1.0),
                sample(timestamp = 2_000L, heartRate = 90, x = 2.0, y = 2.0, z = 2.0)
            ),
            childName = "Bennett"
        )

        assertTrue(alerts.isEmpty())
    }

    private fun sample(
        timestamp: Long,
        heartRate: Int?,
        x: Double = 0.0,
        y: Double = 0.0,
        z: Double = 0.0
    ) = AlertSensorSample(
        timestamp = timestamp,
        heartRate = heartRate,
        accelerationX = x,
        accelerationY = y,
        accelerationZ = z
    )
}

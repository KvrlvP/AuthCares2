package com.choque.authcares2.features.stats.report

import com.choque.authcares2.features.stats.ActivityLevel
import com.choque.authcares2.features.stats.StatisticsPeriod
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class StatisticsReportFormatterTest {

    private val timeZone = TimeZone.getTimeZone("America/Bogota")
    private val formatter = PlainTextStatisticsReportFormatter(
        locale = Locale.forLanguageTag("es"),
        timeZone = timeZone
    )

    @Test
    fun dailyReportIncludesDateAndCalculatedMetrics() {
        val text = formatter.format(report(StatisticsPeriod.TODAY))

        assertTrue(text.contains("Resumen Diario - AuthCares"))
        assertTrue(text.contains("Fecha: 29/06/2026"))
        assertTrue(text.contains("FC promedio: 82 BPM"))
        assertTrue(text.contains("Mediciones registradas: 145"))
    }

    @Test
    fun weeklyReportIncludesCorrespondingDateRange() {
        val text = formatter.format(report(StatisticsPeriod.WEEK))

        assertTrue(text.contains("Resumen Semanal - AuthCares"))
        assertTrue(text.contains("Semana: 23/06/2026 - 29/06/2026"))
    }

    @Test
    fun monthlyReportIncludesCorrespondingMonth() {
        val text = formatter.format(report(StatisticsPeriod.MONTH))

        assertTrue(text.contains("Resumen Mensual - AuthCares"))
        assertTrue(text.contains("Mes: Junio de 2026"))
        assertTrue(text.contains("Actividad: Moderada"))
    }

    private fun report(period: StatisticsPeriod): StatisticsReport {
        return StatisticsReport(
            period = period,
            childName = "Bennett",
            generatedAt = Calendar.getInstance(timeZone).apply {
                set(2026, Calendar.JUNE, 29, 12, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis,
            averageHeartRate = 82,
            maximumHeartRate = 104,
            minimumHeartRate = 71,
            activityLevel = ActivityLevel.MODERATE,
            measurementCount = 145,
            automaticSummary = "Resumen calculado con el historial."
        )
    }
}

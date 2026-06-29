package com.choque.authcares2.features.stats.report

import com.choque.authcares2.features.stats.ActivityLevel
import com.choque.authcares2.features.stats.StatisticsPeriod
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class StatisticsReport(
    val period: StatisticsPeriod,
    val childName: String,
    val generatedAt: Long,
    val averageHeartRate: Int,
    val maximumHeartRate: Int,
    val minimumHeartRate: Int,
    val activityLevel: ActivityLevel,
    val measurementCount: Int,
    val automaticSummary: String
)

fun interface StatisticsReportFormatter {
    fun format(report: StatisticsReport): String
}

class PlainTextStatisticsReportFormatter(
    private val locale: Locale = Locale.forLanguageTag("es"),
    private val timeZone: TimeZone = TimeZone.getDefault()
) : StatisticsReportFormatter {

    override fun format(report: StatisticsReport): String {
        return buildString {
            appendLine(titleFor(report.period))
            appendLine()
            appendLine("👦 Niño: ${report.childName}")
            appendLine("📅 ${periodLabel(report.period)}: ${formattedPeriod(report)}")
            appendLine()
            appendLine("❤️ FC promedio: ${report.averageHeartRate} BPM")
            appendLine("📈 FC máxima: ${report.maximumHeartRate} BPM")
            appendLine("📉 FC mínima: ${report.minimumHeartRate} BPM")
            appendLine()
            appendLine("🏃 Actividad: ${report.activityLevel.displayName}")
            appendLine()
            appendLine("📊 Mediciones registradas: ${report.measurementCount}")
            appendLine()
            appendLine("📝 Resumen")
            appendLine(report.automaticSummary)
            appendLine()
            append("Generado por AuthCares.")
        }
    }

    private fun titleFor(period: StatisticsPeriod): String = when (period) {
        StatisticsPeriod.TODAY -> "📊 Resumen Diario - AuthCares"
        StatisticsPeriod.WEEK -> "📊 Resumen Semanal - AuthCares"
        StatisticsPeriod.MONTH -> "📊 Resumen Mensual - AuthCares"
    }

    private fun periodLabel(period: StatisticsPeriod): String = when (period) {
        StatisticsPeriod.TODAY -> "Fecha"
        StatisticsPeriod.WEEK -> "Semana"
        StatisticsPeriod.MONTH -> "Mes"
    }

    private fun formattedPeriod(report: StatisticsReport): String = when (report.period) {
        StatisticsPeriod.TODAY -> formatDate(report.generatedAt, "dd/MM/yyyy")
        StatisticsPeriod.WEEK -> {
            val end = startOfDay(report.generatedAt)
            val start = Calendar.getInstance(timeZone, locale).apply {
                timeInMillis = end
                add(Calendar.DAY_OF_MONTH, -6)
            }.timeInMillis
            "${formatDate(start, "dd/MM/yyyy")} - ${formatDate(end, "dd/MM/yyyy")}"
        }
        StatisticsPeriod.MONTH -> formatDate(report.generatedAt, "MMMM 'de' yyyy")
            .replaceFirstChar { it.uppercase(locale) }
    }

    private fun startOfDay(value: Long): Long {
        return Calendar.getInstance(timeZone, locale).apply {
            timeInMillis = value
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun formatDate(value: Long, pattern: String): String {
        return SimpleDateFormat(pattern, locale).apply {
            timeZone = this@PlainTextStatisticsReportFormatter.timeZone
        }.format(Date(value))
    }
}

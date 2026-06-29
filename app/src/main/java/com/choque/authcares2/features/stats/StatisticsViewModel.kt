package com.choque.authcares2.features.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choque.authcares2.features.stats.data.FirebaseStatisticsRepository
import com.choque.authcares2.features.stats.data.StatisticsRepository
import com.choque.authcares2.features.stats.model.HistoryMeasurement
import com.choque.authcares2.features.stats.model.SensorVector
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

enum class StatisticsPeriod(val displayName: String) {
    TODAY("Hoy"),
    WEEK("Semana"),
    MONTH("Mes")
}

enum class ActivityLevel(val displayName: String) {
    LOW("Baja"),
    MODERATE("Moderada"),
    HIGH("Alta")
}

data class HeartRateBar(
    val label: String,
    val average: Int?
)

data class StatisticsUiState(
    val selectedPeriod: StatisticsPeriod = StatisticsPeriod.TODAY,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val averageHeartRate: Int? = null,
    val maximumHeartRate: Int? = null,
    val minimumHeartRate: Int? = null,
    val measurementCount: Int = 0,
    val lastMeasurementHeartRate: Int? = null,
    val lastMeasurementTimestamp: Long? = null,
    val activityLevel: ActivityLevel? = null,
    val chartBars: List<HeartRateBar> = emptyList(),
    val summary: String = "",
    val hasData: Boolean = false
)

class StatisticsViewModel(
    private val repository: StatisticsRepository = FirebaseStatisticsRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    private var history: List<HistoryMeasurement> = emptyList()
    private var childName: String = "el niño"
    private var observedWatchId: String? = null
    private var historyJob: Job? = null

    fun observeWatch(watchId: String?, childName: String) {
        this.childName = childName.ifBlank { "el niño" }

        if (watchId.isNullOrBlank()) {
            observedWatchId = null
            historyJob?.cancel()
            history = emptyList()
            _uiState.value = emptyState(
                selectedPeriod = _uiState.value.selectedPeriod,
                message = "No hay un reloj conectado."
            )
            return
        }

        if (observedWatchId == watchId && historyJob?.isActive == true) {
            recalculate()
            return
        }

        observedWatchId = watchId
        historyJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        historyJob = viewModelScope.launch {
            repository.observeHistory(watchId).collect { result ->
                result.onSuccess { measurements ->
                    history = measurements
                    recalculate()
                }.onFailure {
                    _uiState.value = emptyState(
                        selectedPeriod = _uiState.value.selectedPeriod,
                        message = "No pudimos leer el historial del reloj."
                    )
                }
            }
        }
    }

    fun selectPeriod(period: StatisticsPeriod) {
        if (_uiState.value.selectedPeriod == period) return
        _uiState.update { it.copy(selectedPeriod = period, isLoading = true) }
        recalculate()
    }

    private fun recalculate() {
        val period = _uiState.value.selectedPeriod
        val now = System.currentTimeMillis()
        val periodStart = periodStart(period, now)
        val periodMeasurements = history.filter { it.timestamp in periodStart..now }
        val validHeartRates = periodMeasurements.mapNotNull { measurement ->
            measurement.heartRate?.takeIf { it > 0 }
        }

        if (validHeartRates.isEmpty()) {
            _uiState.value = emptyState(
                selectedPeriod = period,
                chartBars = buildChartBars(period, periodMeasurements, now)
            )
            return
        }

        val average = validHeartRates.average().toInt()
        val maximum = validHeartRates.maxOrNull()
        val minimum = validHeartRates.minOrNull()
        val lastMeasurement = periodMeasurements
            .filter { it.heartRate != null && it.heartRate > 0 }
            .maxByOrNull { it.timestamp }
        val activityLevel = calculateActivityLevel(periodMeasurements, history)

        _uiState.value = StatisticsUiState(
            selectedPeriod = period,
            isLoading = false,
            averageHeartRate = average,
            maximumHeartRate = maximum,
            minimumHeartRate = minimum,
            measurementCount = validHeartRates.size,
            lastMeasurementHeartRate = lastMeasurement?.heartRate,
            lastMeasurementTimestamp = lastMeasurement?.timestamp,
            activityLevel = activityLevel,
            chartBars = buildChartBars(period, periodMeasurements, now),
            summary = buildSummary(
                period = period,
                average = average,
                maximum = maximum,
                minimum = minimum,
                activityLevel = activityLevel
            ),
            hasData = true
        )
    }

    private fun calculateActivityLevel(
        selectedMeasurements: List<HistoryMeasurement>,
        allMeasurements: List<HistoryMeasurement>
    ): ActivityLevel? {
        val selectedChanges = movementChanges(selectedMeasurements)
        if (selectedChanges.isEmpty()) return null

        val baselineChanges = movementChanges(allMeasurements)
        if (baselineChanges.isEmpty()) return null

        val baselineAcceleration = median(
            baselineChanges.map { it.first }.filter { it > 0.0 }
        )
        val baselineGyroscope = median(
            baselineChanges.map { it.second }.filter { it > 0.0 }
        )

        val normalizedScores = selectedChanges.map { (acceleration, gyroscope) ->
            val parts = buildList {
                if (baselineAcceleration != null) add(acceleration / baselineAcceleration)
                if (baselineGyroscope != null) add(gyroscope / baselineGyroscope)
            }
            if (parts.isEmpty()) 0.0 else parts.average()
        }
        val score = normalizedScores.average()

        return when {
            score < 0.75 -> ActivityLevel.LOW
            score < 1.5 -> ActivityLevel.MODERATE
            else -> ActivityLevel.HIGH
        }
    }

    private fun movementChanges(
        measurements: List<HistoryMeasurement>
    ): List<Pair<Double, Double>> {
        return measurements.sortedBy { it.timestamp }.zipWithNext()
            .mapNotNull { (previous, current) ->
                val accelerationChange = vectorDistance(
                    previous.acceleration,
                    current.acceleration
                )
                val gyroscopeChange = vectorDistance(
                    previous.gyroscope,
                    current.gyroscope
                )
                if (accelerationChange == null && gyroscopeChange == null) {
                    null
                } else {
                    (accelerationChange ?: 0.0) to (gyroscopeChange ?: 0.0)
                }
            }
    }

    private fun vectorDistance(first: SensorVector?, second: SensorVector?): Double? {
        if (first == null || second == null) return null
        val x = second.x - first.x
        val y = second.y - first.y
        val z = second.z - first.z
        return sqrt(x * x + y * y + z * z)
    }

    private fun median(values: List<Double>): Double? {
        if (values.isEmpty()) return null
        val sorted = values.sorted()
        val middle = sorted.size / 2
        return if (sorted.size % 2 == 0) {
            (sorted[middle - 1] + sorted[middle]) / 2.0
        } else {
            sorted[middle]
        }
    }

    private fun buildChartBars(
        period: StatisticsPeriod,
        measurements: List<HistoryMeasurement>,
        now: Long
    ): List<HeartRateBar> {
        return when (period) {
            StatisticsPeriod.TODAY -> buildTodayBars(measurements, now)
            StatisticsPeriod.WEEK -> buildWeekBars(measurements, now)
            StatisticsPeriod.MONTH -> buildMonthBars(measurements, now)
        }
    }

    private fun buildTodayBars(
        measurements: List<HistoryMeasurement>,
        now: Long
    ): List<HeartRateBar> {
        val start = startOfDay(now)
        return (0 until 6).map { index ->
            val bucketStart = start + index * FOUR_HOURS
            val bucketEnd = bucketStart + FOUR_HOURS
            HeartRateBar(
                label = SimpleDateFormat("ha", Locale.getDefault())
                    .format(Date(bucketStart))
                    .lowercase(),
                average = averageForRange(measurements, bucketStart, bucketEnd)
            )
        }
    }

    private fun buildWeekBars(
        measurements: List<HistoryMeasurement>,
        now: Long
    ): List<HeartRateBar> {
        val start = periodStart(StatisticsPeriod.WEEK, now)
        return (0 until 7).map { index ->
            val bucketStart = start + index * DAY
            val bucketEnd = bucketStart + DAY
            HeartRateBar(
                label = SimpleDateFormat("EEE", Locale("es"))
                    .format(Date(bucketStart))
                    .take(2)
                    .replaceFirstChar { it.uppercase() },
                average = averageForRange(measurements, bucketStart, bucketEnd)
            )
        }
    }

    private fun buildMonthBars(
        measurements: List<HistoryMeasurement>,
        now: Long
    ): List<HeartRateBar> {
        val calendar = Calendar.getInstance().apply { timeInMillis = now }
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val monthStart = periodStart(StatisticsPeriod.MONTH, now)
        return (0 until ((daysInMonth + 6) / 7)).map { index ->
            val firstDay = index * 7 + 1
            val lastDay = minOf(firstDay + 6, daysInMonth)
            val bucketStart = monthStart + index * 7 * DAY
            val bucketEnd = if (lastDay == daysInMonth) {
                startOfNextMonth(now)
            } else {
                bucketStart + 7 * DAY
            }
            HeartRateBar(
                label = "$firstDay-$lastDay",
                average = averageForRange(measurements, bucketStart, bucketEnd)
            )
        }
    }

    private fun averageForRange(
        measurements: List<HistoryMeasurement>,
        start: Long,
        endExclusive: Long
    ): Int? {
        val values = measurements
            .filter { it.timestamp >= start && it.timestamp < endExclusive }
            .mapNotNull { it.heartRate?.takeIf { value -> value > 0 } }
        return values.takeIf { it.isNotEmpty() }?.average()?.toInt()
    }

    private fun periodStart(period: StatisticsPeriod, now: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        when (period) {
            StatisticsPeriod.TODAY -> Unit
            StatisticsPeriod.WEEK -> calendar.add(Calendar.DAY_OF_MONTH, -6)
            StatisticsPeriod.MONTH -> calendar.set(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }

    private fun startOfDay(value: Long): Long =
        periodStart(StatisticsPeriod.TODAY, value)

    private fun startOfNextMonth(now: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MONTH, 1)
        }.timeInMillis
    }

    private fun buildSummary(
        period: StatisticsPeriod,
        average: Int,
        maximum: Int?,
        minimum: Int?,
        activityLevel: ActivityLevel?
    ): String {
        val periodText = when (period) {
            StatisticsPeriod.TODAY -> "Hoy"
            StatisticsPeriod.WEEK -> "Durante los últimos 7 días"
            StatisticsPeriod.MONTH -> "Durante este mes"
        }
        val activityText = activityLevel?.displayName?.lowercase()
            ?: "sin datos suficientes"
        return "$periodText $childName presentó una frecuencia cardíaca promedio de " +
            "$average BPM, con un máximo de ${maximum ?: "--"} BPM y un mínimo de " +
            "${minimum ?: "--"} BPM. Su nivel de actividad fue $activityText durante " +
            "el monitoreo."
    }

    private fun emptyState(
        selectedPeriod: StatisticsPeriod,
        message: String? = null,
        chartBars: List<HeartRateBar> = emptyList()
    ) = StatisticsUiState(
        selectedPeriod = selectedPeriod,
        isLoading = false,
        errorMessage = message,
        chartBars = chartBars,
        summary = "No hay suficientes mediciones para generar el resumen."
    )

    private companion object {
        const val DAY = 24L * 60L * 60L * 1000L
        const val FOUR_HOURS = 4L * 60L * 60L * 1000L
    }
}

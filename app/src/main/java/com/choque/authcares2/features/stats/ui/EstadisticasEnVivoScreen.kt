package com.choque.authcares2.features.stats.ui
import android.widget.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.choque.authcares2.R
import com.choque.authcares2.features.stats.HeartRateBar
import com.choque.authcares2.features.stats.StatisticsPeriod
import com.choque.authcares2.features.stats.StatisticsUiState
import com.choque.authcares2.features.stats.StatisticsViewModel
import com.choque.authcares2.features.stats.StatisticsUiEvent
import com.choque.authcares2.features.stats.share.AndroidSummarySharer
import com.choque.authcares2.navigation.AuthCaresScreen
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryFixed
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface
import com.choque.authcares2.ui.theme.MetricHeartBg
import com.choque.authcares2.ui.theme.MetricTempBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EstadisticasAuthCaresScreen(
    childName: String,
    watchId: String?,
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {}
) {
    EstadisticasEnVivoScreen(
        childName = childName,
        watchId = watchId,
        modifier = modifier,
        onNavigateTo = onNavigateTo
    )
}

@Composable
fun EstadisticasEnVivoScreen(
    childName: String,
    watchId: String?,
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {},
    statisticsViewModel: StatisticsViewModel = viewModel()
) {
    val state by statisticsViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(watchId, childName) {
        statisticsViewModel.observeWatch(watchId, childName)
    }
    LaunchedEffect(statisticsViewModel, context) {
        statisticsViewModel.events.collect { event ->
            when (event) {
                is StatisticsUiEvent.ShareSummary ->
                    AndroidSummarySharer.share(context, event.text)
                is StatisticsUiEvent.ShowMessage ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ChildCard(childName)
            PeriodFilters(
                selected = state.selectedPeriod,
                onSelected = statisticsViewModel::selectPeriod
            )

            if (state.isLoading) {
                LoadingCard()
            } else {
                HeartRateChart(state.chartBars)
                MetricsGrid(state)
                AutomaticSummary(
                    summary = state.summary,
                    hasData = state.hasData,
                    onShare = statisticsViewModel::shareSummary
                )
            }
        }
    }
}

@Composable
private fun ChildCard(childName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(AuthCaresSecondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = childName.take(1).uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AuthCaresOnPrimary
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = childName,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AuthCaresOnSurface
            )
            Text(
                text = "Seguimiento activo",
                fontSize = 14.sp,
                color = AuthCaresOnSurfaceVariant
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_authcares_menu),
            contentDescription = null,
            tint = AuthCaresOnSurfaceVariant
        )
    }
}

@Composable
private fun PeriodFilters(
    selected: StatisticsPeriod,
    onSelected: (StatisticsPeriod) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        StatisticsPeriod.entries.forEach { period ->
            val isSelected = period == selected
            Button(
                onClick = { onSelected(period) },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) {
                        AuthCaresPrimary
                    } else {
                        Color.Transparent
                    },
                    contentColor = if (isSelected) {
                        AuthCaresOnPrimary
                    } else {
                        AuthCaresOnSurfaceVariant
                    }
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isSelected) 4.dp else 0.dp
                )
            ) {
                Text(
                    text = period.displayName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LoadingCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(AuthCaresWhiteSurface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AuthCaresPrimary)
    }
}

@Composable
private fun HeartRateChart(bars: List<HeartRateBar>) {
    val values = bars.mapNotNull { it.average }
    val minimum = values.minOrNull()
    val maximum = values.maxOrNull()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(AuthCaresWhiteSurface)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Ritmo cardíaco promedio",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AuthCaresOnSurface
                )
                Text(
                    text = "Promedio por intervalo",
                    fontSize = 12.sp,
                    color = AuthCaresOnSurfaceVariant
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_authcares_heart),
                contentDescription = null,
                tint = AuthCaresSecondary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (values.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay mediciones para este período",
                    fontSize = 14.sp,
                    color = AuthCaresOnSurfaceVariant
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                bars.forEach { bar ->
                    HeartRateChartBar(
                        bar = bar,
                        fraction = chartFraction(bar.average, minimum, maximum),
                        highlighted = bar.average != null && bar.average == maximum,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeartRateChartBar(
    bar: HeartRateBar,
    fraction: Float,
    highlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = bar.average?.toString() ?: "—",
            fontSize = 10.sp,
            color = AuthCaresOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((100 * fraction).dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                    .background(
                        when {
                            bar.average == null -> AuthCaresSurface
                            highlighted -> AuthCaresPrimary
                            else -> AuthCaresSecondaryContainer
                        }
                    )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = bar.label,
            fontSize = 9.sp,
            color = AuthCaresOnSurfaceVariant,
            maxLines = 1
        )
    }
}

private fun chartFraction(value: Int?, minimum: Int?, maximum: Int?): Float {
    if (value == null) return 0.04f
    if (minimum == null || maximum == null || minimum == maximum) return 0.55f
    val normalized = (value - minimum).toFloat() / (maximum - minimum).toFloat()
    return 0.25f + normalized * 0.55f
}

@Composable
private fun MetricsGrid(state: StatisticsUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        state.errorMessage?.let { EmptyMessage(it) }

        MetricRow(
            first = MetricInfo(
                "Promedio",
                state.averageHeartRate?.toString() ?: "—",
                "BPM",
                R.drawable.ic_authcares_heart
            ),
            second = MetricInfo(
                "Máximo",
                state.maximumHeartRate?.toString() ?: "—",
                "BPM",
                R.drawable.ic_authcares_stats
            )
        )
        MetricRow(
            first = MetricInfo(
                "Mínimo",
                state.minimumHeartRate?.toString() ?: "—",
                "BPM",
                R.drawable.ic_authcares_heart
            ),
            second = MetricInfo(
                "Mediciones",
                if (state.hasData) state.measurementCount.toString() else "—",
                "registros",
                R.drawable.ic_authcares_check_circle
            )
        )
        MetricRow(
            first = MetricInfo(
                "Última medición",
                state.lastMeasurementHeartRate?.toString() ?: "—",
                state.lastMeasurementTimestamp?.let(::formatMeasurementTime)
                    ?: "sin datos",
                R.drawable.ic_authcares_calendar
            ),
            second = MetricInfo(
                "Actividad",
                state.activityLevel?.displayName ?: "—",
                if (state.activityLevel == null) "sin datos" else "nivel",
                R.drawable.ic_authcares_running
            )
        )
    }
}

private data class MetricInfo(
    val title: String,
    val value: String,
    val unit: String,
    val icon: Int
)

@Composable
private fun MetricRow(first: MetricInfo, second: MetricInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MetricCard(first, MetricHeartBg, Modifier.weight(1f))
        MetricCard(second, MetricTempBg, Modifier.weight(1f))
    }
}

@Composable
private fun MetricCard(
    metric: MetricInfo,
    background: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(AuthCaresWhiteSurface)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(metric.icon),
                    contentDescription = null,
                    tint = AuthCaresSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = metric.title,
                fontSize = 12.sp,
                color = AuthCaresOnSurfaceVariant,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = metric.value,
            fontSize = if (metric.value.length > 5) 22.sp else 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AuthCaresOnSurface,
            maxLines = 1
        )
        Text(
            text = metric.unit,
            fontSize = 11.sp,
            color = AuthCaresOnSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
private fun EmptyMessage(message: String) {
    Text(
        text = message,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .padding(16.dp),
        fontSize = 14.sp,
        color = AuthCaresOnSurfaceVariant
    )
}

@Composable
private fun AutomaticSummary(
    summary: String,
    hasData: Boolean,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(AuthCaresPrimaryFixed)
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AuthCaresPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_authcares_smile),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = "Resumen para compartir",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AuthCaresPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = summary,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = AuthCaresOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onShare,
            enabled = hasData,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthCaresPrimary,
                contentColor = AuthCaresOnPrimary
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_authcares_share),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "COMPARTIR RESUMEN",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun formatMeasurementTime(timestamp: Long): String =
    SimpleDateFormat("dd MMM, HH:mm", Locale.forLanguageTag("es")).format(Date(timestamp))

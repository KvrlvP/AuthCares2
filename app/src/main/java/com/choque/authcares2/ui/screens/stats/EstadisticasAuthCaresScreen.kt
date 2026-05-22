package com.choque.authcares2.ui.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.AuthCaresScreen
import com.choque.authcares2.R
import com.choque.authcares2.ui.components.HomeBottomBar
import com.choque.authcares2.ui.components.HomeTab
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryFixed
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface
import com.choque.authcares2.ui.theme.MetricHeartBg
import com.choque.authcares2.ui.theme.MetricTempBg

@Composable
fun EstadisticasAuthCaresScreen(
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(HomeTab.Horarios) } // Corregido a Horarios
    var selectedFilter by remember { mutableStateOf(TimeFilter.Hoy) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // TopBar Estadísticas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .background(AuthCaresSurface)
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { onNavigateTo(AuthCaresScreen.Home) }) { // <--- Volver al Home
                        Icon(
                            painter = painterResource(R.drawable.ic_authcares_menu),
                            contentDescription = null,
                            tint = AuthCaresOnSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        text = "AuthCares",
                        color = AuthCaresPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Image(
                        painter = painterResource(R.drawable.avatar_elena),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.dp, AuthCaresOutlineVariant, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp, bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    ChildSelector()
                    TimeFilters(selectedFilter = selectedFilter, onFilterClick = { selectedFilter = it })
                    ActivityChart()
                    MetricsGrid()
                    ShareableSummary()
                }
            }

            HomeBottomBar(
                selectedTab = selectedTab,
                onTabClick = { tab ->
                    selectedTab = tab
                    // --- CONEXIÓN DE NAVEGACIÓN ---
                    when (tab) {
                        HomeTab.Inicio -> onNavigateTo(AuthCaresScreen.Home)
                        HomeTab.Horarios -> onNavigateTo(AuthCaresScreen.Stats)
                        HomeTab.Ninos -> onNavigateTo(AuthCaresScreen.Kids)
                        HomeTab.Ajustes -> onNavigateTo(AuthCaresScreen.Settings)
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

// --- COMPONENTES DE ESTADÍSTICAS ---

@Composable
private fun ChildSelector(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.avatar_lucas),
            contentDescription = null,
            modifier = Modifier.size(48.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(text = "Leo", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
            Text(text = "3 años", fontSize = 14.sp, color = AuthCaresOnSurfaceVariant)
        }
        Icon(
            painter = painterResource(R.drawable.ic_authcares_menu),
            contentDescription = null,
            tint = AuthCaresOnSurfaceVariant
        )
    }
}

@Composable
private fun TimeFilters(
    selectedFilter: TimeFilter,
    onFilterClick: (TimeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TimeFilterButton("Hoy", TimeFilter.Hoy, selectedFilter, onFilterClick, Modifier.weight(1f))
        TimeFilterButton("Semana", TimeFilter.Semana, selectedFilter, onFilterClick, Modifier.weight(1f))
        TimeFilterButton("Mes", TimeFilter.Mes, selectedFilter, onFilterClick, Modifier.weight(1f))
    }
}

@Composable
private fun TimeFilterButton(
    text: String,
    filter: TimeFilter,
    selectedFilter: TimeFilter,
    onFilterClick: (TimeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = filter == selectedFilter
    Button(
        onClick = { onFilterClick(filter) },
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) AuthCaresPrimary else Color.Transparent,
            contentColor = if (isSelected) AuthCaresOnPrimary else AuthCaresOnSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ActivityChart(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
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
            Text(text = "Actividad habitual", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
            Icon(
                painter = painterResource(R.drawable.ic_authcares_bell),
                contentDescription = null,
                tint = AuthCaresSecondary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            ChartBar(0.20f, false, modifier = Modifier.weight(1f))
            ChartBar(0.40f, false, modifier = Modifier.weight(1f))
            ChartBar(0.95f, true, modifier = Modifier.weight(1f))
            ChartBar(0.65f, false, modifier = Modifier.weight(1f))
            ChartBar(0.35f, false, modifier = Modifier.weight(1f))
            ChartBar(0.15f, false, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("8am", fontSize = 12.sp, color = AuthCaresOnSurfaceVariant)
            Text("12pm", fontSize = 12.sp, color = AuthCaresOnSurfaceVariant)
            Text("4pm", fontSize = 12.sp, color = AuthCaresOnSurfaceVariant)
            Text("8pm", fontSize = 12.sp, color = AuthCaresOnSurfaceVariant)
        }
    }
}

@Composable
private fun ChartBar(fraction: Float, isHighlighted: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight(fraction)
            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            .background(if (isHighlighted) AuthCaresPrimary else AuthCaresSecondaryContainer)
    )
}

@Composable
private fun MetricsGrid(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        MetricCard(
            title = "Ritmo cardíaco estable",
            value = "98 bpm",
            icon = R.drawable.ic_authcares_heart,
            bgColor = MetricHeartBg, // <--- Importado del tema
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            title = "Temperatura",
            value = "36.5° C",
            icon = R.drawable.ic_authcares_thermostat,
            bgColor = MetricTempBg, // <--- Importado del tema
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: Int,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(AuthCaresWhiteSurface)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(icon), contentDescription = null, tint = AuthCaresSecondary, modifier = Modifier.size(18.dp))
            }
            Text(text = title, fontSize = 12.sp, color = AuthCaresOnSurfaceVariant, fontWeight = FontWeight.Bold, lineHeight = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value.split(" ").first(), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = AuthCaresOnSurface)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = value.split(" ").last(), fontSize = 14.sp, color = AuthCaresOnSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
        }
    }
}

@Composable
private fun ShareableSummary(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(AuthCaresPrimaryFixed)
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(AuthCaresPrimary), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(R.drawable.ic_authcares_smile), contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Text(text = "Resumen para compartir", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Todo se ve muy bien hoy. Leo ha mantenido un ritmo cardíaco tranquilo y su nivel de actividad es el habitual para una tarde de juegos. Se nota descansado y feliz.",
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = AuthCaresOnSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AuthCaresPrimary, contentColor = AuthCaresOnPrimary)
        ) {
            Icon(painter = painterResource(R.drawable.ic_authcares_share), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "COMPARTIR REPORTE", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private enum class TimeFilter { Hoy, Semana, Mes }

@Preview(showBackground = true)
@Composable
private fun EstadisticasAuthCaresScreenPreview() {
    AuthCares2Theme {
        EstadisticasAuthCaresScreen()
    }
}
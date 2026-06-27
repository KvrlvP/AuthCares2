package com.choque.authcares2.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant2
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainer
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionAlertasScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize(), color = AuthCaresSurface) {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = { Text(text = "Alertas Inteligentes", color = AuthCaresSecondary, fontSize = 22.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(painter = painterResource(R.drawable.ic_authcares_menu), contentDescription = "Volver", tint = AuthCaresOnSurfaceVariant, modifier = Modifier.size(28.dp)) } },
                actions = { Box(modifier = Modifier.size(48.dp)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AuthCaresSurface)
            )

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(text = "Configuración de Umbrales", fontSize = 25.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
                Text(text = "Ajusta los niveles en los que deseas recibir notificaciones.", fontSize = 16.sp, color = AuthCaresOnSurfaceVariant, lineHeight = 20.sp)
                HeartRateThresholdCard()
                TemperatureThresholdCard()
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = AuthCaresPrimary, contentColor = AuthCaresOnPrimary)) { Text(text = "Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun HeartRateThresholdCard(modifier: Modifier = Modifier) {
    var highAlertValue by remember { mutableFloatStateOf(115f) }
    var lowAlertValue by remember { mutableFloatStateOf(60f) }
    Column(modifier = modifier.fillMaxWidth().border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(32.dp)).background(AuthCaresWhiteSurface).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFFFEBEE)), contentAlignment = Alignment.Center) { Icon(painter = painterResource(R.drawable.ic_authcares_heart), contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(22.dp)) }
            Column { Text(text = "Ritmo Cardíaco", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface); Text(text = "Latidos por minuto (bpm)", fontSize = 14.sp, color = AuthCaresOnSurfaceVariant) }
        }
        InfoBox("Para un niño en reposo, se sugiere alertar si supera los 110 bpm.")
        ThresholdSlider(label = "Alerta Alta", value = highAlertValue, onValueChange = { highAlertValue = it }, valueLabel = "${highAlertValue.toInt()} bpm")
        ThresholdSlider(label = "Alerta Baja", value = lowAlertValue, onValueChange = { lowAlertValue = it }, valueLabel = "${lowAlertValue.toInt()} bpm")
    }
}

@Composable
private fun TemperatureThresholdCard(modifier: Modifier = Modifier) {
    var tempValue by remember { mutableFloatStateOf(37.8f) }
    Column(modifier = modifier.fillMaxWidth().border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(32.dp)).background(AuthCaresWhiteSurface).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFFFF3E0)), contentAlignment = Alignment.Center) { Icon(painter = painterResource(R.drawable.ic_authcares_thermostat), contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(22.dp)) }
            Column { Text(text = "Temperatura Corporal", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface); Text(text = "Grados Celsius (°C)", fontSize = 14.sp, color = AuthCaresOnSurfaceVariant) }
        }
        InfoBox("Se recomienda alertar si la temperatura corporal supera los 37.5 °C.")
        ThresholdSlider(label = "Alerta Alta", value = tempValue, onValueChange = { tempValue = it }, valueLabel = "${String.format("%.1f", tempValue)} °C")
    }
}

@Composable
private fun InfoBox(text: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(AuthCaresPrimaryContainer).padding(16.dp)) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(painter = painterResource(R.drawable.ic_authcares_help), contentDescription = null, tint = AuthCaresPrimary, modifier = Modifier.size(20.dp))
            Text(text = text, fontSize = 14.sp, lineHeight = 20.sp, color = AuthCaresOnSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThresholdSlider(label: String, value: Float, onValueChange: (Float) -> Unit, valueLabel: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, fontSize = 16.sp, color = AuthCaresOnSurface, fontWeight = FontWeight.Medium)
            Text(text = valueLabel, fontSize = 18.sp, color = AuthCaresPrimary, fontWeight = FontWeight.Bold)
        }
        Slider(value = value, onValueChange = onValueChange, colors = SliderDefaults.colors(activeTrackColor = AuthCaresOnSurfaceVariant2, inactiveTrackColor = AuthCaresSurfaceContainer), modifier = Modifier.fillMaxWidth(), thumb = {
            Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(AuthCaresPrimary).shadow(elevation = 4.dp, shape = CircleShape))
        })
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfiguracionAlertasScreenPreview() {
    AuthCares2Theme {
        ConfiguracionAlertasScreen()
    }
}
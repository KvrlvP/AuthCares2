package com.choque.authcares2.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.navigation.AuthCaresScreen
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryFixed
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryFixed
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainerHigh
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface
import com.choque.authcares2.core.model.SensorUiState

@Composable
fun InicioAuthCaresScreen(
    userName: String = "Usuario",
    sensorState: SensorUiState = SensorUiState(),
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {},
    onChildClick: (com.choque.authcares2.core.model.ChildInfo) -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                GreetingSection(userName = userName)
                LiveSensorSection(sensorState = sensorState)
                StatusGrid(sensorState = sensorState)

                RegisteredKidsSection(
                    children = sensorState.registeredChildren,
                    onChildClick = onChildClick
                )
            }

            // BOTÓN FLOTANTE DE ASISTENTE IA
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 24.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 12.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.3f))
            ) {
                IconButton(
                    onClick = { onNavigateTo(AuthCaresScreen.AI) },
                    modifier = Modifier.size(64.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fab_asistente_ia),
                        contentDescription = "Asistente IA",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GreetingSection(userName: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "¡Hola, $userName!",
            color = AuthCaresOnSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.02).sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Todo está listo para hoy.",
            color = AuthCaresOnSurfaceVariant,
            fontSize = 18.sp,
            lineHeight = 26.sp
        )
    }
}

@Composable
private fun LiveSensorSection(sensorState: SensorUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .border(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (sensorState.childName.isBlank()) "Sensores del reloj" else sensorState.childName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuthCaresOnSurface
                )
                Text(
                    text = sensorState.relojId?.let { "Reloj $it" } ?: "Sin reloj vinculado",
                    fontSize = 12.sp,
                    color = AuthCaresOnSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(statusColor(sensorState.status).copy(alpha = 0.14f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = sensorState.status,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor(sensorState.status)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SensorValueCard(
                title = "Ritmo",
                value = sensorState.heartRate?.let { "$it bpm" } ?: "--",
                modifier = Modifier.weight(1f)
            )
            SensorValueCard(
                title = "Movimiento",
                value = sensorState.movement,
                modifier = Modifier.weight(1f)
            )
        }

        val message = sensorState.errorMessage
            ?: sensorState.lastSync.takeIf { it.isNotBlank() }
            ?: if (sensorState.isLoading) "Conectando con Firebase..." else ""
        if (message.isNotBlank()) {
            Text(
                text = message,
                fontSize = 12.sp,
                color = AuthCaresOnSurfaceVariant
            )
        }
    }
}

@Composable
private fun SensorValueCard(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(AuthCaresSurfaceContainerHigh)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AuthCaresOnSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AuthCaresOnSurface
        )
    }
}

private fun statusColor(status: String): Color {
    return when (status) {
        "Todo bien" -> Color(0xFF10B981)
        "Revisar" -> Color(0xFFDC2626)
        else -> AuthCaresSecondary
    }
}

@Composable
private fun StatusGrid(sensorState: SensorUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatusCardWatch(
            isConnected = sensorState.relojId != null,
            modifier = Modifier.fillMaxWidth()
        )
        StatusCardSync(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun StatusCardWatch(isConnected: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .border(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(AuthCaresPrimaryFixed),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.ic_authcares_watch), contentDescription = null, tint = AuthCaresPrimary, modifier = Modifier.size(24.dp))
            }
            Column {
                Text(
                    text = if (isConnected) "Reloj conectado" else "Sin reloj",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AuthCaresOnSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val batteryColor = if (isConnected) Color(0xFF10B981) else Color.Gray
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(batteryColor))
                    Text(
                        text = if (isConnected) "Batería --%" else "Sin datos",
                        fontSize = 12.sp,
                        color = AuthCaresOnSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AuthCaresSecondary, contentColor = Color.White)
        ) {
            Text(text = "Configurar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StatusCardSync(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .border(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(AuthCaresSecondaryFixed),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.ic_authcares_cloud), contentDescription = null, tint = AuthCaresSecondary, modifier = Modifier.size(24.dp))
            }
            Column {
                Text(text = "Sincronizado", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
                Text(text = "Firebase hace 2 min", fontSize = 12.sp, color = AuthCaresOnSurfaceVariant)
            }
        }
    }
}

@Composable
private fun RegisteredKidsSection(
    children: List<com.choque.authcares2.core.model.ChildInfo>,
    onChildClick: (com.choque.authcares2.core.model.ChildInfo) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Niños registrados",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = AuthCaresOnSurface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (children.isEmpty()) {
                Text(
                    text = "No hay niños registrados aún.",
                    fontSize = 14.sp,
                    color = AuthCaresOnSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                children.forEach { child ->
                    KidAvatar(
                        name = child.name,
                        avatarRes = child.avatarRes,
                        onClick = { onChildClick(child) }
                    )
                }
            }
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AuthCaresSecondary, contentColor = AuthCaresOnPrimary)
        ) {
            Icon(painter = painterResource(R.drawable.ic_authcares_person_add), contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Agregar niño", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun KidAvatar(
    name: String,
    avatarRes: Int?,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(AuthCaresWhiteSurface)
            .border(1.dp, AuthCaresOutlineVariant.copy(alpha=0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable(onClick = onClick)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (avatarRes != null) {
            Image(
                painter = painterResource(avatarRes),
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(CircleShape).border(2.dp, AuthCaresPrimaryFixed, CircleShape)
            )
        } else {
            Box(
                modifier = Modifier.size(64.dp).clip(CircleShape).background(AuthCaresSurfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_authcares_person),
                    contentDescription = null,
                    tint = AuthCaresOnSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AuthCaresOnSurface)
    }
}

@Preview(showBackground = true)
@Composable
private fun InicioAuthCaresScreenPreview() {
    AuthCares2Theme {
        InicioAuthCaresScreen()
    }
}

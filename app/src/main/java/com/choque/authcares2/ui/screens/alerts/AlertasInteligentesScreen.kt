package com.choque.authcares2.ui.screens.alerts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.AuthCaresScreen
import com.choque.authcares2.R
import com.choque.authcares2.ui.components.HomeBottomBar
import com.choque.authcares2.ui.components.HomeTab
import com.choque.authcares2.ui.model.AlertItem
import com.choque.authcares2.ui.theme.AlertBlue
import com.choque.authcares2.ui.theme.AlertOrange
import com.choque.authcares2.ui.theme.AlertRed
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresErrorContainer
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryFixed
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainer
import com.choque.authcares2.ui.theme.AuthCaresTertiaryFixed
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

// ✅ EL CÓDIGO ESTÁ LIMPIO: NO HAY DEFINICIONES LOCALES DE COLORES NI CLASES AQUÍ.

@Composable
fun AlertasInteligentesScreen(
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(HomeTab.Inicio) }

    // Usamos la clase AlertItem que importamos del paquete model
    val alerts = listOf(
        AlertItem(
            childName = "Lucas",
            alertType = "Aumento de ritmo cardíaco",
            description = "El ritmo cardíaco se ha mantenido elevado por más de 5 minutos durante el periodo de descanso.",
            time = "10:30 AM",
            iconRes = R.drawable.ic_authcares_heart,
            iconTint = AlertRed, // Viene del tema
            iconBg = AuthCaresErrorContainer,
            priorityColor = AlertRed,
            borderColor = AuthCaresErrorContainer
        ),
        AlertItem(
            childName = "Sofía",
            alertType = "Temperatura ligeramente alta",
            description = "La temperatura registrada es 1°C mayor al promedio matutino habitual.",
            time = "08:15 AM",
            iconRes = R.drawable.ic_authcares_thermostat,
            iconTint = AlertOrange,
            iconBg = AuthCaresTertiaryFixed, // Viene del tema
            priorityColor = AlertOrange,
            borderColor = AuthCaresTertiaryFixed
        ),
        AlertItem(
            childName = "Lucas",
            alertType = "Actividad inusual detectada",
            description = "Patrón de movimiento repetitivo detectado durante la clase de arte.",
            time = "Ayer, 4:45 PM",
            iconRes = R.drawable.ic_authcares_running,
            iconTint = AlertBlue,
            iconBg = AuthCaresSecondaryFixed, // Viene del tema
            priorityColor = AlertBlue,
            borderColor = AuthCaresSurfaceContainer
        )
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                AlertsTopBar()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp, bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    AlertsHeader()

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(alerts) { alert ->
                            AlertCard(
                                alert = alert,
                                onClick = { onNavigateTo(AuthCaresScreen.AlertDetail) }
                            )
                        }
                    }
                }
            }

            HomeBottomBar(
                selectedTab = selectedTab,
                onTabClick = { tab ->
                    selectedTab = tab
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

@Composable
private fun AlertsTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(AuthCaresSurfaceContainer)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.ic_authcares_menu),
                contentDescription = null,
                tint = AuthCaresPrimary,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = "AuthCares",
            color = AuthCaresPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.02).sp
        )

        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape
        ) {
            Image(
                painter = painterResource(R.drawable.avatar_elena),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun AlertsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Alertas",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = AuthCaresOnSurface
        )
        Icon(
            painter = painterResource(R.drawable.ic_authcares_notifications_active),
            contentDescription = null,
            tint = AuthCaresPrimary,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun AlertCard(alert: AlertItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = AuthCaresWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, alert.borderColor.copy(alpha = 0.5f)),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(alert.priorityColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = alert.childName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurfaceVariant)
                    Text(text = alert.time, fontSize = 14.sp, color = AuthCaresOutlineVariant)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = alert.alertType, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = alert.description, fontSize = 14.sp, lineHeight = 20.sp, color = AuthCaresOnSurfaceVariant)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AuthCaresSurfaceContainer,
                        contentColor = AuthCaresOnSurfaceVariant
                    ),
                    border = androidx.compose.foundation.BorderStroke(0.dp, Color.Transparent)
                ) {
                    Text(text = "Ver detalles", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun AlertIconCircle(alert: AlertItem) {
    Box(
        modifier = Modifier.size(48.dp).clip(CircleShape).background(alert.iconBg),
        contentAlignment = Alignment.Center
    ) {
        Icon(painter = painterResource(alert.iconRes), contentDescription = null, tint = alert.iconTint, modifier = Modifier.size(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun AlertasInteligentesScreenPreview() {
    AuthCares2Theme {
        AlertasInteligentesScreen(
            onNavigateTo = {}
        )
    }
}
package com.choque.authcares2.ui.screens.alerts

import androidx.compose.foundation.background
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

@Composable
fun AlertasInteligentesScreen(
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {}
) {
    val alerts = listOf(
        AlertItem(
            childName = "Lucas",
            alertType = "Aumento de ritmo cardíaco",
            description = "El ritmo cardíaco se ha mantenido elevado por más de 5 minutos durante el periodo de descanso.",
            time = "10:30 AM",
            iconRes = R.drawable.ic_authcares_heart,
            iconTint = AlertRed,
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
            iconBg = AuthCaresTertiaryFixed,
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
            iconBg = AuthCaresSecondaryFixed,
            priorityColor = AlertBlue,
            borderColor = AuthCaresSurfaceContainer
        )
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopBar específico de Alertas con botón atrás
            AlertsTopBar(onBackClick = { onNavigateTo(AuthCaresScreen.Home) })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AlertsHeader()

                LazyColumn(
                    modifier = Modifier.weight(1f),
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
    }
}

@Composable
private fun AlertsTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(AuthCaresSurface)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.ic_authcares_arrow_back),
                contentDescription = "Volver",
                tint = AuthCaresPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Alertas Inteligentes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AuthCaresPrimary
        )
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
            text = "Centro de Notificaciones",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AuthCaresOnSurface
        )
        Icon(
            painter = painterResource(R.drawable.ic_authcares_notifications_active),
            contentDescription = null,
            tint = AuthCaresPrimary,
            modifier = Modifier.size(24.dp)
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

@Preview(showBackground = true)
@Composable
private fun AlertasInteligentesScreenPreview() {
    AuthCares2Theme {
        AlertasInteligentesScreen(
            onNavigateTo = {}
        )
    }
}

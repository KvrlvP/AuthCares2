package com.choque.authcares2.features.alerts.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.R
import com.choque.authcares2.features.alerts.model.AlertItem
import com.choque.authcares2.features.alerts.model.AlertType
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AlertasInteligentesScreen(
    alerts: List<AlertItem>,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAlertClick: (AlertItem) -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AlertsTopBar(onBackClick = onBackClick)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AlertsHeader()

                when {
                    isLoading -> AlertStateCard("Leyendo el historial del reloj...")
                    errorMessage != null -> AlertStateCard(errorMessage)
                    alerts.isEmpty() -> AlertStateCard("No hay alertas detectadas en el historial.")
                    else -> LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(alerts, key = { it.id }) { alert ->
                            AlertCard(
                                alert = alert,
                                onClick = { onAlertClick(alert) }
                            )
                        }
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
private fun AlertStateCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = AuthCaresWhiteSurface,
        border = BorderStroke(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.3f)),
        shadowElevation = 2.dp
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(20.dp),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = AuthCaresOnSurfaceVariant
        )
    }
}

@Composable
private fun AlertCard(alert: AlertItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val style = alertStyle(alert.type)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = AuthCaresWhiteSurface,
        border = BorderStroke(1.dp, style.borderColor.copy(alpha = 0.5f)),
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
                    .background(style.priorityColor)
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
                    Text(text = formatAlertTime(alert.endedAt), fontSize = 14.sp, color = AuthCaresOutlineVariant)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = alert.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(style.iconBg, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(style.iconRes),
                            contentDescription = null,
                            tint = style.iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = alert.description,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = AuthCaresOnSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AuthCaresSurfaceContainer,
                        contentColor = AuthCaresOnSurfaceVariant
                    ),
                    border = BorderStroke(0.dp, Color.Transparent)
                ) {
                    Text(text = "Ver detalles", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

private data class AlertVisualStyle(
    val iconRes: Int,
    val iconTint: Color,
    val iconBg: Color,
    val priorityColor: Color,
    val borderColor: Color
)

private fun alertStyle(type: AlertType): AlertVisualStyle = when (type) {
    AlertType.HEART_RATE -> AlertVisualStyle(
        iconRes = R.drawable.ic_authcares_heart,
        iconTint = AlertRed,
        iconBg = AuthCaresErrorContainer,
        priorityColor = AlertRed,
        borderColor = AuthCaresErrorContainer
    )
    AlertType.INTENSE_MOVEMENT -> AlertVisualStyle(
        iconRes = R.drawable.ic_authcares_running,
        iconTint = AlertBlue,
        iconBg = AuthCaresSecondaryFixed,
        priorityColor = AlertBlue,
        borderColor = AuthCaresSurfaceContainer
    )
    AlertType.COMBINED -> AlertVisualStyle(
        iconRes = R.drawable.ic_authcares_notifications_active,
        iconTint = AlertOrange,
        iconBg = AuthCaresTertiaryFixed,
        priorityColor = AlertOrange,
        borderColor = AuthCaresTertiaryFixed
    )
}

private fun formatAlertTime(timestamp: Long): String = SimpleDateFormat(
    "dd MMM, HH:mm",
    Locale.forLanguageTag("es")
).format(Date(timestamp))

@Preview(showBackground = true)
@Composable
private fun AlertasInteligentesScreenPreview() {
    AuthCares2Theme {
        AlertasInteligentesScreen(
            alerts = emptyList(),
            isLoading = false,
            errorMessage = null
        )
    }
}

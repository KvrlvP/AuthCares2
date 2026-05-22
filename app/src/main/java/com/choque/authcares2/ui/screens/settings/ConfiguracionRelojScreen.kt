package com.choque.authcares2.ui.screens.settings

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainer
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainerLow
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionRelojScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isConnected by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize(), color = AuthCaresSurface) {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = { Text(text = "Reloj Inteligente", color = AuthCaresSecondary, fontSize = 22.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(painter = painterResource(R.drawable.ic_authcares_menu), contentDescription = "Volver", tint = AuthCaresOnSurfaceVariant, modifier = Modifier.size(28.dp)) } },
                actions = { Box(modifier = Modifier.size(48.dp)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AuthCaresSurface)
            )

            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Reloj inteligente", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = AuthCaresOnSurface)
                    Text(text = "Configura y gestiona la conexión con el dispositivo del niño.", fontSize = 16.sp, color = AuthCaresOnSurfaceVariant, lineHeight = 22.sp)
                }
                ConnectionStatusCard(isConnected = isConnected)
                InfoBoxReloj()
                Spacer(modifier = Modifier.weight(1f))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { isConnected = true }, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = AuthCaresPrimaryContainer, contentColor = AuthCaresOnPrimary), enabled = !isConnected) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_watch), contentDescription = null, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Vincular reloj", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = { isConnected = false }, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = AuthCaresSecondary.copy(alpha = if (isConnected) 1f else 0.5f), contentColor = Color.White, disabledContentColor = Color.White), enabled = isConnected) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_watch), contentDescription = null, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Desvincular", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectionStatusCard(isConnected: Boolean, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(32.dp)).clip(RoundedCornerShape(32.dp)).background(AuthCaresWhiteSurface).padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
            if (!isConnected) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val scale by infiniteTransition.animateFloat(initialValue = 0.8f, targetValue = 1.4f, animationSpec = infiniteRepeatable(animation = tween(1500), repeatMode = RepeatMode.Restart), label = "ringScale")
                val alpha by infiniteTransition.animateFloat(initialValue = 0.4f, targetValue = 0f, animationSpec = infiniteRepeatable(animation = tween(1500), repeatMode = RepeatMode.Restart), label = "ringAlpha")
                Box(modifier = Modifier.fillMaxSize().scale(scale).clip(CircleShape).background(AuthCaresPrimary.copy(alpha = alpha)))
            }
            Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(if (isConnected) AuthCaresPrimaryContainer else AuthCaresSurfaceContainerLow), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(R.drawable.ic_authcares_watch), contentDescription = null, tint = if (isConnected) AuthCaresOnPrimary else AuthCaresPrimary, modifier = Modifier.size(56.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = if (isConnected) "Conectado" else "No conectado", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = if (isConnected) AuthCaresPrimary else AuthCaresOnSurface)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = if (isConnected) "Sincronización activa con AuthCares." else "El reloj está desconectado o fuera de alcance.", fontSize = 14.sp, color = AuthCaresOnSurfaceVariant, textAlign = TextAlign.Center)
    }
}

@Composable
private fun InfoBoxReloj(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(24.dp)).background(AuthCaresWhiteSurface).padding(16.dp)) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(AuthCaresSecondaryContainer), contentAlignment = Alignment.Center) { Icon(painter = painterResource(R.drawable.ic_authcares_help), contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp)) }
            Text(text = "El reloj permitirá enviar información para el seguimiento del niño. Las estadísticas no estarán disponibles en esta versión móvil.", fontSize = 14.sp, lineHeight = 20.sp, color = AuthCaresOnSurfaceVariant)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfiguracionRelojScreenPreview() {
    AuthCares2Theme {
        ConfiguracionRelojScreen()
    }
}
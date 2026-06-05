package com.choque.authcares2.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import com.choque.authcares2.AuthCaresScreen
import com.choque.authcares2.R
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
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainerHigh
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

@Composable
fun InicioCentralizadoScreen(
    userName: String = "Usuario",
    childName: String = "tu niño",
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {}
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
                    .padding(top = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                GreetingSectionCentralized(userName = userName, childName = childName)
                MainBentoGrid(childName = childName)
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 24.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 12.dp,
                border = BorderStroke(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.3f))
            ) {
                IconButton(
                    onClick = { onNavigateTo(AuthCaresScreen.AI) },
                    modifier = Modifier.size(56.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fab_asistente_ia),
                        contentDescription = "Asistente IA",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GreetingSectionCentralized(userName: String, childName: String) {
    Column {
        Text(
            text = "¡Hola, $userName!",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.02).sp,
            color = AuthCaresOnSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Aquí está el resumen del día para $childName.",
            fontSize = 16.sp,
            color = AuthCaresOnSurfaceVariant
        )
    }
}

@Composable
private fun MainBentoGrid(childName: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LucasMainCard(childName = childName)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            StatusMiniCard(
                title = "RELOJ",
                value = "Conectado",
                icon = R.drawable.ic_authcares_watch,
                isImage = true
            )
            StatusMiniCard(
                title = "ESTADO GENERAL",
                value = "Tranquilo",
                icon = R.drawable.ic_authcares_smile,
                iconBg = Color(0xFFE6F4EA),
                iconTint = Color(0xFF1E8E3E)
            )
            StatusMiniCard(
                title = "ÚLTIMA SYNC",
                value = "Hace 2 min",
                icon = R.drawable.ic_authcares_cloud,
                iconBg = AuthCaresSurfaceContainerHigh,
                iconTint = AuthCaresOnSurfaceVariant
            )
        }
    }
}

@Composable
private fun LucasMainCard(childName: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = AuthCaresWhiteSurface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier.size(96.dp).clip(CircleShape).background(AuthCaresSecondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = childName.take(1).uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuthCaresOnPrimary
                )
            }
            Column {
                Text(text = childName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AuthCaresOnSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Seguimiento activo", fontSize = 14.sp, color = AuthCaresOnSurfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuthCaresSecondary,
                        contentColor = AuthCaresOnPrimary
                    ),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Icon(painter = painterResource(R.drawable.ic_authcares_school), contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Está en clase ahora", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun StatusMiniCard(title: String, value: String, icon: Int, iconBg: Color = AuthCaresPrimaryFixed, iconTint: Color = AuthCaresPrimary, isImage: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = AuthCaresWhiteSurface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(icon), contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
            Column {
                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AuthCaresOnSurfaceVariant, letterSpacing = 0.05.sp)
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
            }
        }
    }
}

@Composable
private fun QuickAccessSectionCentralized() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Accesos rápidos", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickButton(title = "Mis hijos", icon = R.drawable.ic_authcares_family, bgColor = Color(0x1A6200EE), tint = AuthCaresPrimary, modifier = Modifier.weight(1f))
            QuickButton(title = "Estadísticas", icon = R.drawable.ic_authcares_stats, bgColor = Color(0x3303DAC5), tint = AuthCaresSecondary, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickButton(title = "Reloj", icon = R.drawable.ic_authcares_watch, bgColor = Color(0xFFF5ECE4), tint = Color(0xFF9D4D00), modifier = Modifier.weight(1f))
            QuickButton(title = "Compartir", icon = R.drawable.ic_authcares_share, bgColor = AuthCaresSurfaceContainerHigh, tint = AuthCaresOnSurfaceVariant, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun QuickButton(title: String, icon: Int, bgColor: Color, tint: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(56.dp).clip(CircleShape).background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = tint, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AuthCaresOnSurface, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
private fun InicioCentralizadoScreenPreview() {
    AuthCares2Theme {
        InicioCentralizadoScreen()
    }
}

package com.choque.authcares2.ui.screens.profile

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.navigation.AuthCaresScreen
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresErrorContainer
import com.choque.authcares2.ui.theme.AuthCaresOnErrorContainer
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryFixed
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainerHigh
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

@Composable
fun PerfilAuthCaresScreen(
    userName: String = "Usuario",
    userEmail: String = "",
    childName: String = "",
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
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
            ProfileCard(userName = userName, userEmail = userEmail, childName = childName)
            PrivacyAlertCard()
            SettingsGrid()
            LogoutButton(onClick = onLogout)
        }
    }
}


@Composable
private fun ProfileCard(
    modifier: Modifier = Modifier,
    userName: String,
    userEmail: String,
    childName: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(AuthCaresWhiteSurface)
            .border(1.dp, AuthCaresSurfaceContainerHigh, RoundedCornerShape(32.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AuthCaresPrimaryFixed.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(AuthCaresSecondaryContainer)
                    .border(4.dp, AuthCaresSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuthCaresOnPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userName,
                color = AuthCaresOnSurface,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = userEmail.ifBlank { "Sin correo" },
                color = AuthCaresOnSurfaceVariant,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(AuthCaresPrimaryFixed.copy(alpha = 0.2f))
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (childName.isNotBlank()) "Cuidador de $childName" else "Cuidador Responsable",
                    color = AuthCaresPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PrivacyAlertCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresSurfaceContainerHigh)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AuthCaresPrimary)
        )

        Icon(
            painter = painterResource(R.drawable.ic_authcares_lock),
            contentDescription = null,
            tint = AuthCaresPrimary,
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = "La información registrada es sensible y debe manejarse con cuidado.",
            color = AuthCaresOnSurface,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SettingsGrid(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SettingsCard(title = "Editar Perfil", icon = R.drawable.ic_authcares_edit, modifier = Modifier.weight(1f))
            SettingsCard(title = "Notificaciones", icon = R.drawable.ic_authcares_bell, modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SettingsCard(title = "Seguridad", icon = R.drawable.ic_authcares_lock, modifier = Modifier.weight(1f))
            SettingsCard(title = "Ayuda", icon = R.drawable.ic_authcares_help, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    icon: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .border(1.dp, AuthCaresSurfaceContainerHigh, RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        CircleIconProfile(
            icon = icon,
            background = AuthCaresPrimaryFixed,
            tint = AuthCaresPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            color = AuthCaresOnSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthCaresErrorContainer,
            contentColor = AuthCaresOnErrorContainer
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_authcares_logout),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Cerrar sesión",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CircleIconProfile(
    icon: Int,
    background: Color,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PerfilAuthCaresScreenPreview() {
    AuthCares2Theme {
        PerfilAuthCaresScreen()
    }
}

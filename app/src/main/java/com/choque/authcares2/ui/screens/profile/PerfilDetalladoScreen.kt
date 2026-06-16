package com.choque.authcares2.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.TextButton
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
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface
import com.choque.authcares2.viewmodels.ChildInfo

@Composable
fun PerfilDetalladoScreen(
    child: ChildInfo?,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onNavigateTo: (com.choque.authcares2.AuthCaresScreen) -> Unit = {}
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
            ) {
                PerfilTopBar(onBackClick = onBackClick)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ProfileHeaderCard(child = child)
                    QuickActionsSection()
                    WatchConnectionCard(relojId = child?.relojId, onNavigateTo = onNavigateTo)
                    SupportNetworkSection()
                    EditInfoButton()
                }
            }

            // BOTÓN FLOTANTE DE ASISTENTE IA
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 24.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 12.dp,
                border = BorderStroke(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.3f))
            ) {
                IconButton(
                    onClick = { onNavigateTo(com.choque.authcares2.AuthCaresScreen.AI) },
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
private fun PerfilTopBar(onBackClick: () -> Unit) { // RECIBE PARAMETRO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(AuthCaresSurface)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) { // USA EL PARAMETRO
            Icon(
                painter = painterResource(R.drawable.ic_authcares_arrow_back),
                contentDescription = null,
                tint = AuthCaresPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = "Perfil del Niño",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AuthCaresPrimary
        )
        Spacer(modifier = Modifier.width(48.dp))
    }
}

@Composable
private fun ProfileHeaderCard(child: ChildInfo?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = AuthCaresWhiteSurface,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(AuthCaresSecondaryContainer)
                    .border(4.dp, AuthCaresSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = child?.name?.take(1)?.uppercase() ?: "?",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuthCaresOnPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = child?.name ?: "Sin nombre",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AuthCaresOnSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PillInfo(
                    icon = R.drawable.ic_authcares_cake,
                    text = child?.fechaNacimiento ?: "Sin fecha"
                )
                PillInfo(
                    icon = R.drawable.ic_authcares_psychology,
                    text = "TEA Nivel ${child?.nivelTea ?: "-"}",
                    bgColor = AuthCaresSecondaryContainer,
                    textColor = AuthCaresOnSurface
                )
            }
        }
    }
}

@Composable
private fun PillInfo(
    icon: Int,
    text: String,
    bgColor: Color = AuthCaresSurfaceContainer,
    textColor: Color = AuthCaresOnSurfaceVariant
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = textColor
        )
    }
}

@Composable
private fun QuickActionsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Acciones rápidas",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AuthCaresOnSurface
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionButton(title = "Llamar Colegio", icon = R.drawable.ic_authcares_call, modifier = Modifier.weight(1f))
            ActionButton(title = "Mensaje Profesor", icon = R.drawable.ic_authcares_chat, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ActionButton(title: String, icon: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresSurfaceContainer)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = AuthCaresPrimary, modifier = Modifier.size(28.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AuthCaresOnSurface, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Composable
private fun WatchConnectionCard(
    relojId: String?,
    onNavigateTo: (com.choque.authcares2.AuthCaresScreen) -> Unit
) {
    val isConnected = !relojId.isNullOrBlank()
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = if (isConnected) AuthCaresPrimary else AuthCaresOutlineVariant,
        shadowElevation = 12.dp
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                        Image(painter = painterResource(R.drawable.ic_authcares_watch), contentDescription = null, modifier = Modifier.size(30.dp))
                    }
                    Column {
                        Text(text = if (isConnected) "RELOJ VINCULADO" else "SIN RELOJ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = relojId ?: "----", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
                Row(modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(Color.White.copy(alpha = 0.15f)).padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (isConnected) Color(0xFF4ADE80) else Color.Gray))
                    Text(text = if (isConnected) "Conectado" else "Desconectado", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (isConnected) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { onNavigateTo(com.choque.authcares2.AuthCaresScreen.Stats) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = AuthCaresPrimary)) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_stats), contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Ver estadísticas", fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = { onNavigateTo(com.choque.authcares2.AuthCaresScreen.Share) }, modifier = Modifier.size(54.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.15f))) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_share), contentDescription = null, tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun SupportNetworkSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Red de apoyo", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
        ContactCard(title = "Colegio", name = "Colegio San Martín", icon = R.drawable.ic_authcares_school)
        ContactCard(title = "Profesor", name = "Ana López", subtitle = "1º Básico A", icon = R.drawable.ic_authcares_teacher)
        ContactCard(title = "Psicólogo", name = "Dr. Roberto Sánchez", icon = R.drawable.ic_authcares_psychology)
    }
}

@Composable
private fun ContactCard(title: String, name: String, subtitle: String? = null, icon: Int) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), color = AuthCaresWhiteSurface, shadowElevation = 6.dp) {
        Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(AuthCaresPrimaryContainer), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(icon), contentDescription = null, tint = AuthCaresPrimary, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AuthCaresOutlineVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
                subtitle?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = it, fontSize = 14.sp, color = AuthCaresOnSurfaceVariant)
                }
            }
            IconButton(onClick = {}) { Icon(painter = painterResource(R.drawable.ic_authcares_more), contentDescription = null, tint = AuthCaresOnSurfaceVariant) }
        }
    }
}

@Composable
private fun EditInfoButton() {
    Button(onClick = {}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50.dp), border = BorderStroke(2.dp, AuthCaresPrimary), colors = ButtonDefaults.buttonColors(containerColor = AuthCaresWhiteSurface, contentColor = AuthCaresPrimary), contentPadding = PaddingValues(vertical = 16.dp)) {
        Icon(painter = painterResource(R.drawable.ic_authcares_edit), contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Editar información", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}

package com.choque.authcares2.features.share.ui

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.choque.authcares2.ui.theme.AuthCaresPrimaryFixed
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainer
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompartirAuthCaresScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedRole by remember { mutableIntStateOf(0) }

    Surface(modifier = modifier.fillMaxSize(), color = AuthCaresSurface) {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = { Text(text = "Compartir Información", color = AuthCaresSecondary, fontSize = 22.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(painter = painterResource(R.drawable.ic_authcares_menu), contentDescription = "Volver", tint = AuthCaresOnSurfaceVariant, modifier = Modifier.size(28.dp)) } },
                actions = { Box(modifier = Modifier.size(48.dp)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AuthCaresSurface)
            )

            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(AuthCaresPrimaryFixed), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(R.drawable.ic_authcares_share), contentDescription = null, tint = AuthCaresPrimary, modifier = Modifier.size(28.dp))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Compartir información", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = AuthCaresOnSurface, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Elige qué información deseas compartir de forma segura.", fontSize = 16.sp, color = AuthCaresOnSurfaceVariant, textAlign = TextAlign.Center)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RoleCard(icon = R.drawable.ic_authcares_school, title = "Profesor", isSelected = selectedRole == 0, onClick = { selectedRole = 0 }, modifier = Modifier.weight(1f))
                    RoleCard(icon = R.drawable.ic_authcares_psychology, title = "Psicólogo", isSelected = selectedRole == 1, onClick = { selectedRole = 1 }, modifier = Modifier.weight(1f))
                    RoleCard(icon = R.drawable.ic_authcares_family, title = "Familiar", isSelected = selectedRole == 2, onClick = { selectedRole = 2 }, modifier = Modifier.weight(1f))
                }
                SecurePreviewCard()
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(28.dp), colors = ButtonDefaults.buttonColors(containerColor = AuthCaresPrimary, contentColor = AuthCaresOnPrimary)) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_share), contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Compartir resumen por WhatsApp", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(28.dp), border = ButtonDefaults.outlinedButtonBorder(enabled = true), colors = ButtonDefaults.outlinedButtonColors(contentColor = AuthCaresPrimary)) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_watch), contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Copiar código del reloj", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(28.dp)) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_visibility), contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Vista previa del reporte", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AuthCaresPrimary)
                    }
                    TextButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(28.dp)) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_pdf), contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Generar reporte PDF", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AuthCaresPrimary)
                    }
                }
                Text(text = "Esta información está protegida y solo se compartirá con el destinatario elegido.", fontSize = 14.sp, color = AuthCaresOnSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
private fun RoleCard(icon: Int, title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val backgroundColor = if (isSelected) AuthCaresPrimaryContainer else AuthCaresWhiteSurface
    val contentColor = if (isSelected) AuthCaresOnPrimary else AuthCaresOnSurfaceVariant
    val borderColor = if (isSelected) AuthCaresPrimary else AuthCaresOutlineVariant

    Column(modifier = modifier.clip(RoundedCornerShape(24.dp)).background(backgroundColor).border(2.dp, borderColor, RoundedCornerShape(24.dp)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isSelected) AuthCaresWhiteSurface else AuthCaresSurfaceContainer), contentAlignment = Alignment.Center) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = if (isSelected) AuthCaresPrimary else AuthCaresOnSurfaceVariant, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = contentColor, textAlign = TextAlign.Center)
    }
}

@Composable
private fun SecurePreviewCard(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(AuthCaresWhiteSurface).border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(24.dp))) {
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(AuthCaresSecondaryContainer))
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(painter = painterResource(R.drawable.ic_authcares_lock), contentDescription = null, tint = AuthCaresSecondary, modifier = Modifier.size(20.dp))
                Text(text = "Vista previa", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(AuthCaresSurface).border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(12.dp)).padding(16.dp)) {
                Text(text = "\"Resumen semanal de Lucas: Datos estables...\"", fontSize = 14.sp, lineHeight = 20.sp, color = AuthCaresOnSurfaceVariant, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompartirAuthCaresScreenPreview() {
    AuthCares2Theme {
        CompartirAuthCaresScreen()
    }
}
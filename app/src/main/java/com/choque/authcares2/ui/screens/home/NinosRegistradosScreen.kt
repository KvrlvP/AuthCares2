package com.choque.authcares2.ui.screens.home

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
import androidx.compose.material3.OutlinedTextField
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
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresErrorContainer
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainer
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

@Composable
fun NinosRegistradosScreen(
    modifier: Modifier = Modifier,
    onNavigateTo: (AuthCaresScreen) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(HomeTab.Ninos) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                NinosTopBar()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp, bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Niños registrados",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AuthCaresOnSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Gestiona los perfiles de los niños a tu cuidado.",
                            fontSize = 16.sp,
                            color = AuthCaresOnSurfaceVariant
                        )
                    }

                    AddChildCard()

                    ChildCard(
                        initialLetter = "L",
                        name = "Lucas",
                        details = "6 años • TEA Nivel 1",
                        watchStatus = "Sin reloj asignado"
                    )
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
private fun NinosTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(AuthCaresSurface)
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

        Box(
            modifier = Modifier.size(42.dp).clip(CircleShape).background(AuthCaresSurfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_authcares_person),
                contentDescription = null,
                tint = AuthCaresOnSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AddChildCard(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var diagnostico by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Añadir nuevo perfil",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AuthCaresOnSurface
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre") },
            placeholder = { Text("Ej. Lucas") },
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AuthCaresPrimary,
                unfocusedBorderColor = AuthCaresOutlineVariant
            ),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it },
                modifier = Modifier.weight(1f),
                label = { Text("Edad") },
                placeholder = { Text("Años") },
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuthCaresPrimary,
                    unfocusedBorderColor = AuthCaresOutlineVariant
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = diagnostico,
                onValueChange = { diagnostico = it },
                modifier = Modifier.weight(1f),
                label = { Text("Diagnóstico") },
                placeholder = { Text("Seleccionar...") },
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuthCaresPrimary,
                    unfocusedBorderColor = AuthCaresOutlineVariant
                ),
                singleLine = true
            )
        }

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthCaresPrimary,
                contentColor = AuthCaresOnPrimary
            )
        ) {
            Icon(painter = painterResource(R.drawable.ic_authcares_add), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Guardar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ChildCard(
    initialLetter: String,
    name: String,
    details: String,
    watchStatus: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuthCaresWhiteSurface)
            .border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(24.dp))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(96.dp)
                .clip(RoundedCornerShape(bottomStart = 96.dp))
                .background(AuthCaresPrimaryContainer.copy(alpha = 0.2f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape).background(AuthCaresSecondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initialLetter,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AuthCaresOnPrimary
                        )
                    }
                    Column {
                        Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurface)
                        Text(text = details, fontSize = 14.sp, color = AuthCaresOnSurfaceVariant)
                    }
                }

                Row {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(AuthCaresSurfaceContainer)
                    ) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_edit), contentDescription = "Editar", tint = AuthCaresOnSurfaceVariant, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(AuthCaresErrorContainer.copy(alpha = 0.2f))
                    ) {
                        Icon(painter = painterResource(R.drawable.ic_authcares_delete), contentDescription = "Eliminar", tint = Color(0xFFBA1A1A), modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(AuthCaresSurfaceContainer.copy(alpha = 0.5f))
                    .border(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(painter = painterResource(R.drawable.ic_authcares_watch), contentDescription = null, tint = AuthCaresOnSurfaceVariant, modifier = Modifier.size(16.dp))
                Text(text = watchStatus, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AuthCaresOnSurfaceVariant)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NinosRegistradosScreenPreview() {
    AuthCares2Theme {
        NinosRegistradosScreen()
    }
}
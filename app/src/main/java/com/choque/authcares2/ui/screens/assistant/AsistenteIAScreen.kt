package com.choque.authcares2.ui.screens.assistant

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainer
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainerLow
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface
import com.choque.authcares2.ui.theme.AlertRed
import com.choque.authcares2.ui.theme.AuthCaresErrorContainer
import com.choque.authcares2.ui.theme.AuthCaresOnErrorContainer

@Composable
fun AsistenteIAScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ChatTopBar(onBackClick = onBackClick)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DisclaimerCard()
                DateSeparator()
                UserBubble(text = "¿Por qué Lucas estuvo inquieto ayer tarde?", time = "10:42 AM")
                AIBubble()
            }

            ChatInputBar(messageText = messageText, onMessageChange = { messageText = it })
        }
    }
}

@Composable
private fun ChatTopBar(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AuthCaresSurfaceContainerLow,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_authcares_menu),
                    contentDescription = "Volver",
                    tint = AuthCaresPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(AuthCaresPrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_authcares_smile),
                    contentDescription = null,
                    tint = AuthCaresOnPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Asistente AuthCares", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AuthCaresPrimary)
                Text(text = "En línea", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AuthCaresSecondary)
            }
        }
    }
}

@Composable
private fun DisclaimerCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AuthCaresSurfaceContainer)
            .border(1.dp, AuthCaresOutlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(painter = painterResource(R.drawable.ic_authcares_help), contentDescription = null, tint = AuthCaresOnSurfaceVariant, modifier = Modifier.size(16.dp))
        Text(
            text = "Este asistente ofrece sugerencias basadas en datos, no reemplaza el diagnóstico médico profesional.",
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = AuthCaresOnSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DateSeparator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AuthCaresSurfaceContainer
        ) {
            Text(
                text = "HOY",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AuthCaresOnSurfaceVariant
            )
        }
    }
}

@Composable
private fun UserBubble(text: String, time: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 0.dp),
            color = AuthCaresPrimary,
            shadowElevation = 4.dp
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = AuthCaresOnPrimary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = time, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AuthCaresOnSurfaceVariant)
    }
}

@Composable
private fun AIBubble(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.Start
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(AuthCaresPrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.ic_authcares_smile), contentDescription = null, tint = AuthCaresOnPrimary, modifier = Modifier.size(18.dp))
            }

            Column {
                Surface(
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 16.dp),
                    color = AuthCaresSurfaceContainerLow,
                    shadowElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Hola. Noté un aumento en su ritmo cardíaco a las 4:00 PM, coincidiendo con el cambio de actividad en el colegio. Podría ser una respuesta a la sobreestimulación sensorial.",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = AuthCaresOnSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        DataCard()
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "¿Te gustaría ver algunas estrategias de calma recomendadas para él?",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = AuthCaresOnSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "10:43 AM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AuthCaresOnSurfaceVariant)
            }
        }
    }
}

@Composable
private fun DataCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AuthCaresSurface,
        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(AuthCaresErrorContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_authcares_heart),
                    contentDescription = null,
                    tint = AuthCaresOnErrorContainer,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(text = "PICO DE RITMO CARDÍACO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AuthCaresOnSurfaceVariant)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "120 lpm ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AlertRed)
                    Text(text = "@ 16:00", fontSize = 14.sp, color = AuthCaresOnSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AuthCaresSurface,
        shadowElevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip(text = "Estrategias de calma", icon = R.drawable.ic_authcares_smile)
                SuggestionChip(text = "Analizar sueño", icon = R.drawable.ic_authcares_moon)
                SuggestionChip(text = "Preparar transición", icon = R.drawable.ic_authcares_calendar)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 16.dp, bottom = 16.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(R.drawable.ic_authcares_add), contentDescription = null, tint = AuthCaresOnSurfaceVariant)
                }

                TextField(
                    value = messageText,
                    onValueChange = onMessageChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = "Escribe un mensaje...", color = AuthCaresOnSurfaceVariant) },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = AuthCaresSurfaceContainerLow,
                        unfocusedContainerColor = AuthCaresSurfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = AuthCaresPrimary
                    ),
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(painter = painterResource(R.drawable.ic_authcares_mic), contentDescription = null, tint = AuthCaresPrimary)
                        }
                    },
                    maxLines = 3
                )

                Button(
                    onClick = {},
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = AuthCaresPrimary, contentColor = AuthCaresOnPrimary),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Icon(painter = painterResource(R.drawable.ic_authcares_send), contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun SuggestionChip(text: String, icon: Int, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = {},
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = AuthCaresSurfaceContainer,
            contentColor = AuthCaresOnSurfaceVariant
        ),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
    ) {
        Icon(painter = painterResource(icon), contentDescription = null, modifier = Modifier.size(16.dp), tint = AuthCaresPrimary)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
private fun AsistenteIAScreenPreview() {
    AuthCares2Theme {
        AsistenteIAScreen()
    }
}

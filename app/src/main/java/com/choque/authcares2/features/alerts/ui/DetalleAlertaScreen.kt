package com.choque.authcares2.features.alerts.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AlertRed
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresErrorContainer
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSecondaryFixed
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainer
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

@Composable
fun DetalleAlertaScreen(
    onBackClick: () -> Unit = {},
    onMarkRevisedClick: () -> Unit = {},
    onSharePsychologistClick: () -> Unit = {},
    onCallSchoolClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopBar con flecha atrás
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_authcares_arrow_back),
                        contentDescription = "Volver",
                        tint = AuthCaresOnSurface,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Detalle de Alerta",
                    color = AuthCaresOnSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(48.dp)) // Para centrar el título
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // HERO SECTION: Corazón pulsante y título
                AlertHero()

                Spacer(modifier = Modifier.height(24.dp))

                // Tarjeta de Explicación
                ExplanationCard()

                Spacer(modifier = Modifier.weight(1f))

                // Botones de Acción
                ActionButtons(
                    onMarkRevisedClick = onMarkRevisedClick,
                    onSharePsychologistClick = onSharePsychologistClick,
                    onCallSchoolClick = onCallSchoolClick
                )
            }
        }
    }
}

@Composable
private fun AlertHero() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.size(96.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
                    .clip(CircleShape)
                    .background(AuthCaresErrorContainer.copy(alpha = alpha))
            )

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(AuthCaresErrorContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_authcares_heart),
                    contentDescription = null,
                    tint = AlertRed, // Usamos el color del tema
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Text(
            text = "Ritmo cardíaco elevado",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.02).sp,
            color = AuthCaresOnSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(painter = painterResource(R.drawable.ic_authcares_watch), contentDescription = null, tint = AuthCaresOnSurfaceVariant, modifier = Modifier.size(18.dp))
            Text(text = "10:30 AM", fontSize = 16.sp, color = AuthCaresOnSurfaceVariant)
            Text(text = "•", fontSize = 16.sp, color = AuthCaresOnSurfaceVariant)
            Text(text = "5 min", fontSize = 16.sp, color = AuthCaresOnSurfaceVariant)
        }
    }
}

@Composable
private fun ExplanationCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = AuthCaresWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, AuthCaresOutlineVariant.copy(alpha = 0.5f)),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(AuthCaresSecondaryFixed),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_authcares_help),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = "Se detectó un aumento ligero en el ritmo cardíaco de Lucas durante la clase de educación física. Los niveles volvieron a la normalidad en 5 minutos.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = AuthCaresOnSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ActionButtons(
    onMarkRevisedClick: () -> Unit,
    onSharePsychologistClick: () -> Unit,
    onCallSchoolClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = onMarkRevisedClick,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthCaresPrimary,
                contentColor = AuthCaresOnPrimary
            )
        ) {
            Icon(painter = painterResource(R.drawable.ic_authcares_check_circle), contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Marcar como revisado", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = onSharePsychologistClick,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthCaresSecondary,
                contentColor = Color.White
            )
        ) {
            Icon(painter = painterResource(R.drawable.ic_authcares_share), contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Compartir con psicólogo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onCallSchoolClick,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = AuthCaresWhiteSurface,
                contentColor = AuthCaresOnSurface
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, AuthCaresOutlineVariant)
        ) {
            Icon(painter = painterResource(R.drawable.ic_authcares_call), contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Llamar al colegio", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
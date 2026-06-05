package com.choque.authcares2.ui.screens.settings

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresPrimaryContainer
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainerLow

@Composable
fun NingunRelojConectadoScreen(
    watchCode: String = "",
    errorMessage: String? = null,
    isConnecting: Boolean = false,
    onWatchCodeChange: (String) -> Unit = {},
    onConnectClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
                .padding(top = 40.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            PulsingWatch()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Ningun reloj conectado",
                color = AuthCaresOnSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ingresa el código que aparece en el reloj para vincularlo a tu cuenta.",
                color = AuthCaresOnSurfaceVariant,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // Campo para el código del reloj
            OutlinedTextField(
                value = watchCode,
                onValueChange = onWatchCodeChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Código del reloj (ej. ABC123)") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                enabled = !isConnecting,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuthCaresPrimary,
                    unfocusedBorderColor = AuthCaresOnSurfaceVariant.copy(alpha = 0.3f)
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onConnectClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                enabled = !isConnecting && watchCode.isNotBlank(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AuthCaresSecondary,
                    contentColor = AuthCaresOnPrimary
                )
            ) {
                if (isConnecting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AuthCaresOnPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_authcares_watch),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Conectar ahora",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PulsingWatch(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "watchPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.78f,
        targetValue = 1.36f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Restart
        ),
        label = "watchRingScale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.34f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Restart
        ),
        label = "watchRingAlpha"
    )

    Box(
        modifier = modifier.size(190.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .clip(CircleShape)
                .background(AuthCaresPrimary.copy(alpha = alpha))
        )
        Box(
            modifier = Modifier
                .size(138.dp)
                .clip(CircleShape)
                .background(AuthCaresPrimaryContainer.copy(alpha = 0.86f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(AuthCaresSurfaceContainerLow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_authcares_watch),
                    contentDescription = null,
                    tint = AuthCaresPrimary,
                    modifier = Modifier.size(58.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NingunRelojConectadoScreenPreview() {
    AuthCares2Theme {
        NingunRelojConectadoScreen()
    }
}

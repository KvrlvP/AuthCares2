package com.choque.authcares2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.AuthCaresScreen
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresSurface

@Composable
fun HomeTopBar(
    onNavigateTo: (AuthCaresScreen) -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AuthCaresSurface)
            .statusBarsPadding()
            .height(72.dp)
            .padding(horizontal = 16.dp)
    ) {
        // Icono Izquierda (Atrás o Menú)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { onBackClick?.invoke() ?: Unit }) {
                Icon(
                    painter = painterResource(
                        if (onBackClick != null) R.drawable.ic_authcares_arrow_back else R.drawable.ic_authcares_menu
                    ),
                    contentDescription = null,
                    tint = AuthCaresPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Título Central
        Text(
            text = "AuthCares",
            modifier = Modifier.align(Alignment.Center),
            color = AuthCaresPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.02).sp,
            textAlign = TextAlign.Center
        )

        // Iconos Derecha
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.size(40.dp)) {
                IconButton(
                    onClick = { onNavigateTo(AuthCaresScreen.Alerts) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_authcares_bell),
                        contentDescription = null,
                        tint = AuthCaresOnSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBA1A1A))
                )
            }

            Image(
                painter = painterResource(R.drawable.avatar_elena),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, AuthCaresOutlineVariant, CircleShape)
                    .clickable { onNavigateTo(AuthCaresScreen.Settings) }
            )
        }
    }
}

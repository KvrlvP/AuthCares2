package com.choque.authcares2.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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

@Composable
fun InicioAuthCaresScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(HomeTab.Inicio) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                HomeTopBar()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp, bottom = 120.dp)
                        .widthIn(max = 520.dp)
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    GreetingSection()
                    ChildSummaryCard()
                    StatusCards()
                    QuickAccessSection()
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 86.dp)
            )

            HomeBottomBar(
                selectedTab = selectedTab,
                onTabClick = { selectedTab = it },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier
) {
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
                tint = AuthCaresOnSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = "AuthCares",
            color = AuthCaresPrimary,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.ic_authcares_bell),
                        contentDescription = null,
                        tint = AuthCaresOnSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 9.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC62828))
                )
            }

            Image(
                painter = painterResource(R.drawable.avatar_elena),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .border(1.dp, AuthCaresOutlineVariant, CircleShape)
            )
        }
    }
}

@Composable
private fun GreetingSection(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "¡Hola, Elena!",
            color = AuthCaresOnSurface,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Aquí está el resumen del día para Lucas.",
            color = AuthCaresOnSurfaceVariant,
            fontSize = 20.sp,
            lineHeight = 28.sp
        )
    }
}

@Composable
private fun ChildSummaryCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(AuthCaresWhiteSurface)
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.avatar_lucas),
            contentDescription = null,
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .border(3.dp, AuthCaresSurfaceContainerLow, CircleShape)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Lucas",
            color = AuthCaresOnSurface,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tiene 8 años y está en el Colegio San Martín",
            color = AuthCaresOnSurfaceVariant,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(AuthCaresPrimaryContainer)
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_authcares_school),
                contentDescription = null,
                tint = AuthCaresOnPrimary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Está en clase ahora",
                color = AuthCaresOnPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatusCards(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WideInfoCard(
            iconContent = {
                Image(
                    painter = painterResource(R.drawable.watch_blue),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            },
            overline = "RELOJ",
            title = "Conectado"
        )

        WideInfoCard(
            iconContent = {
                CircleIcon(
                    icon = R.drawable.ic_authcares_smile,
                    background = Color(0xFFE0F5E8),
                    tint = Color(0xFF1FB86A)
                )
            },
            overline = "ESTADO GENERAL",
            title = "Tranquilo"
        )

        WideInfoCard(
            iconContent = {
                CircleIcon(
                    icon = R.drawable.ic_authcares_sync,
                    background = AuthCaresSurfaceContainer,
                    tint = AuthCaresOnSurfaceVariant
                )
            },
            overline = "ÚLTIMA SYNC",
            title = "Hace 2 min"
        )
    }
}

@Composable
private fun WideInfoCard(
    iconContent: @Composable () -> Unit,
    overline: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(AuthCaresWhiteSurface)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        iconContent()

        Column {
            Text(
                text = overline,
                color = AuthCaresOnSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = title,
                color = AuthCaresOnSurface,
                fontSize = 24.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun QuickAccessSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Accesos rápidos",
            color = AuthCaresOnSurface,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            QuickAccessCard(
                title = "Mis hijos",
                icon = R.drawable.ic_authcares_group,
                background = Color(0xFFE9F3FF),
                tint = AuthCaresPrimary,
                modifier = Modifier.weight(1f)
            )
            QuickAccessCard(
                title = "Estadísticas",
                icon = R.drawable.ic_authcares_stats,
                background = Color(0xFFE4F1FF),
                tint = AuthCaresPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            QuickAccessCard(
                title = "Reloj",
                icon = R.drawable.ic_authcares_watch,
                background = Color(0xFFF5ECE4),
                tint = Color(0xFF8A4A0A),
                modifier = Modifier.weight(1f)
            )
            QuickAccessCard(
                title = "Compartir",
                icon = R.drawable.ic_authcares_share,
                background = AuthCaresSurfaceContainer,
                tint = AuthCaresOnSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickAccessCard(
    title: String,
    icon: Int,
    background: Color,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .aspectRatio(1.35f)
            .clip(RoundedCornerShape(28.dp))
            .background(AuthCaresWhiteSurface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircleIcon(
            icon = icon,
            background = background,
            tint = tint,
            size = 62.dp,
            iconSize = 32.dp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = title,
            color = AuthCaresOnSurface,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FloatingActionButton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(68.dp)
            .clip(CircleShape)
            .background(AuthCaresPrimary)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_authcares_add_child),
            contentDescription = null,
            tint = AuthCaresOnPrimary,
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
private fun HomeBottomBar(
    selectedTab: HomeTab,
    onTabClick: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(AuthCaresWhiteSurface)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomTabButton(
            tab = HomeTab.Inicio,
            selectedTab = selectedTab,
            icon = R.drawable.ic_authcares_home,
            label = "Inicio",
            onTabClick = onTabClick
        )
        BottomTabButton(
            tab = HomeTab.Horarios,
            selectedTab = selectedTab,
            icon = R.drawable.ic_authcares_calendar,
            label = "Horarios",
            onTabClick = onTabClick
        )
        BottomTabButton(
            tab = HomeTab.Ninos,
            selectedTab = selectedTab,
            icon = R.drawable.ic_authcares_smile,
            label = "Niños",
            onTabClick = onTabClick
        )
        BottomTabButton(
            tab = HomeTab.Ajustes,
            selectedTab = selectedTab,
            icon = R.drawable.ic_authcares_settings,
            label = "Ajustes",
            onTabClick = onTabClick
        )
    }
}

@Composable
private fun BottomTabButton(
    tab: HomeTab,
    selectedTab: HomeTab,
    icon: Int,
    label: String,
    onTabClick: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = tab == selectedTab
    Button(
        onClick = { onTabClick(tab) },
        modifier = modifier
            .width(if (selected) 86.dp else 72.dp)
            .height(58.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = ButtonDefaults.ContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) AuthCaresSecondaryContainer else Color.Transparent,
            contentColor = if (selected) AuthCaresPrimary else AuthCaresOnSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = label,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CircleIcon(
    icon: Int,
    background: Color,
    tint: Color,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 48.dp,
    iconSize: androidx.compose.ui.unit.Dp = 26.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

private enum class HomeTab {
    Inicio,
    Horarios,
    Ninos,
    Ajustes
}

@Preview(showBackground = true)
@Composable
private fun InicioAuthCaresScreenPreview() {
    AuthCares2Theme {
        InicioAuthCaresScreen()
    }
}

package com.choque.authcares2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

// ENUM COMPARTIDO
enum class HomeTab { Inicio, Horarios, Ninos, Ajustes }

// BARRA INFERIOR COMPARTIDA
@Composable
fun HomeBottomBar(selectedTab: HomeTab, onTabClick: (HomeTab) -> Unit, modifier: Modifier = Modifier) {
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
        BottomTabButton(tab = HomeTab.Inicio, selectedTab = selectedTab, icon = R.drawable.ic_authcares_home, label = "Inicio", onTabClick = onTabClick)
        BottomTabButton(tab = HomeTab.Horarios, selectedTab = selectedTab, icon = R.drawable.ic_authcares_calendar, label = "Horarios", onTabClick = onTabClick)
        BottomTabButton(tab = HomeTab.Ninos, selectedTab = selectedTab, icon = R.drawable.ic_authcares_smile, label = "Niños", onTabClick = onTabClick)
        BottomTabButton(tab = HomeTab.Ajustes, selectedTab = selectedTab, icon = R.drawable.ic_authcares_settings, label = "Ajustes", onTabClick = onTabClick)
    }
}

// BOTÓN INTERNO DE LA BARRA
@Composable
private fun BottomTabButton(tab: HomeTab, selectedTab: HomeTab, icon: Int, label: String, onTabClick: (HomeTab) -> Unit, modifier: Modifier = Modifier) {
    val selected = tab == selectedTab
    Button(
        onClick = { onTabClick(tab) },
        modifier = modifier.width(if (selected) 86.dp else 72.dp).height(58.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) AuthCaresSecondaryContainer else Color.Transparent,
            contentColor = if (selected) AuthCaresPrimary else AuthCaresOnSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painter = painterResource(icon), contentDescription = null, modifier = Modifier.size(25.dp))
            Text(text = label, fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
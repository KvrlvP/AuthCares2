package com.choque.authcares2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresSecondaryContainer
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface

enum class HomeTab { Inicio, Horarios, Ninos, Ajustes }

@Composable
fun HomeBottomBar(
    selectedTab: HomeTab,
    onTabClick: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(AuthCaresWhiteSurface)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTabItem(tab = HomeTab.Inicio, selectedTab = selectedTab, icon = R.drawable.ic_authcares_home, label = "Inicio", onTabClick = onTabClick)
            BottomTabItem(tab = HomeTab.Horarios, selectedTab = selectedTab, icon = R.drawable.ic_authcares_calendar, label = "Horarios", onTabClick = onTabClick)
            BottomTabItem(tab = HomeTab.Ninos, selectedTab = selectedTab, icon = R.drawable.ic_authcares_smile, label = "Niños", onTabClick = onTabClick)
            BottomTabItem(tab = HomeTab.Ajustes, selectedTab = selectedTab, icon = R.drawable.ic_authcares_settings, label = "Ajustes", onTabClick = onTabClick)
        }
    }
}

@Composable
private fun RowScope.BottomTabItem(
    tab: HomeTab,
    selectedTab: HomeTab,
    icon: Int,
    label: String,
    onTabClick: (HomeTab) -> Unit
) {
    val selected = tab == selectedTab
    val contentColor = if (selected) AuthCaresPrimary else AuthCaresOnSurfaceVariant
    val containerColor = if (selected) AuthCaresSecondaryContainer.copy(alpha = 0.7f) else Color.Transparent

    Box(
        modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .clickable { onTabClick(tab) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                color = contentColor,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeBottomBarPreview() {
    AuthCares2Theme {
        Box(modifier = Modifier.background(Color.LightGray).padding(16.dp)) {
            HomeBottomBar(
                selectedTab = HomeTab.Inicio,
                onTabClick = {}
            )
        }
    }
}

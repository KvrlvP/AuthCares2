package com.choque.authcares2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresNavigationBorder
import com.choque.authcares2.ui.theme.AuthCaresNavigationGlass
import com.choque.authcares2.ui.theme.AuthCaresNavigationSelected
import com.choque.authcares2.ui.theme.AuthCaresNavigationShadow
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary

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
            .padding(start = 18.dp, end = 18.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(30.dp),
                    clip = false,
                    ambientColor = AuthCaresNavigationShadow,
                    spotColor = AuthCaresNavigationShadow
                )
                .clip(RoundedCornerShape(30.dp))
                .background(AuthCaresNavigationGlass)
                .border(1.dp, AuthCaresNavigationBorder, RoundedCornerShape(30.dp))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTabItem(HomeTab.Inicio, selectedTab, R.drawable.ic_authcares_home, "Inicio", onTabClick)
            BottomTabItem(HomeTab.Horarios, selectedTab, R.drawable.ic_authcares_calendar, "Horarios", onTabClick)
            BottomTabItem(HomeTab.Ninos, selectedTab, R.drawable.ic_authcares_smile, "Niños", onTabClick)
            BottomTabItem(HomeTab.Ajustes, selectedTab, R.drawable.ic_authcares_settings, "Ajustes", onTabClick)
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
    val containerColor = if (selected) AuthCaresNavigationSelected else Color.Transparent

    Box(
        modifier = Modifier
            .weight(1f)
            .height(60.dp)
            .padding(horizontal = 3.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(containerColor)
            .clickable { onTabClick(tab) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(if (selected) 29.dp else 27.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeBottomBarPreview() {
    AuthCares2Theme {
        Box(modifier = Modifier.background(Color(0xFFE7EEF8)).padding(16.dp)) {
            HomeBottomBar(
                selectedTab = HomeTab.Inicio,
                onTabClick = {}
            )
        }
    }
}

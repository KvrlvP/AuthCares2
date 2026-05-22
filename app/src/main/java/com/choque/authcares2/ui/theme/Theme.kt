package com.choque.authcares2.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// NOTA: NO es necesario hacer import de los colores (AuthCaresPrimary, etc.)
// porque están definidos en este mismo paquete (ui.theme).

private val DarkColorScheme = darkColorScheme(
    primary = AuthCaresPrimary,
    secondary = AuthCaresSecondary,
    tertiary = AuthCaresPrimaryContainer,
    surface = AuthCaresSurface,
    onSurface = AuthCaresOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = AuthCaresPrimary,
    onPrimary = AuthCaresOnPrimary,
    primaryContainer = AuthCaresPrimaryContainer,
    secondary = AuthCaresSecondary,
    secondaryContainer = AuthCaresSecondaryContainer,
    surface = AuthCaresSurface,
    onSurface = AuthCaresOnSurface,
    onSurfaceVariant = AuthCaresOnSurfaceVariant,
    outline = AuthCaresOutlineVariant,
    outlineVariant = AuthCaresOutlineVariant,
    errorContainer = AuthCaresErrorContainer,
    onErrorContainer = AuthCaresOnErrorContainer
)

@Composable
fun AuthCares2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
package com.choque.authcares2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.choque.authcares2.ui.screens.assistant.AsistenteIAScreen
import com.choque.authcares2.ui.screens.auth.BienvenidaAuthCaresScreen
import com.choque.authcares2.ui.screens.auth.CrearCuentaAuthCaresScreen
import com.choque.authcares2.ui.screens.auth.InformacionAuthCaresScreen
import com.choque.authcares2.ui.screens.auth.IniciarSesionAuthCaresScreen
import com.choque.authcares2.ui.screens.home.InicioAuthCaresScreen
import com.choque.authcares2.ui.screens.home.InicioCentralizadoScreen
import com.choque.authcares2.ui.screens.home.NinosRegistradosScreen
import com.choque.authcares2.ui.screens.profile.PerfilAuthCaresScreen
import com.choque.authcares2.ui.screens.profile.PerfilDetalladoScreen
import com.choque.authcares2.ui.screens.alerts.AlertasInteligentesScreen
import com.choque.authcares2.ui.screens.alerts.DetalleAlertaScreen
import com.choque.authcares2.ui.screens.settings.ConfiguracionAlertasScreen
import com.choque.authcares2.ui.screens.settings.ConfiguracionRelojScreen
import com.choque.authcares2.ui.screens.share.CompartirAuthCaresScreen
import com.choque.authcares2.ui.screens.stats.EstadisticasAuthCaresScreen
import com.choque.authcares2.ui.theme.AuthCares2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthCares2Theme {
                // Estado de navegación principal
                var currentScreen by remember { mutableStateOf(AuthCaresScreen.Welcome) }

                // Simulación de estados simples para Auth
                var loginEmail by remember { mutableStateOf("") }
                var loginPassword by remember { mutableStateOf("") }
                var registerFullName by remember { mutableStateOf("") }
                var registerEmail by remember { mutableStateOf("") }
                var registerPassword by remember { mutableStateOf("") }

                // Función auxiliar para navegar
                val navigateTo: (AuthCaresScreen) -> Unit = { screen ->
                    currentScreen = screen
                }

                // Función auxiliar para "Atrás" simple
                val onBack: () -> Unit = {
                    currentScreen = when (currentScreen) {
                        // Auth Flow
                        AuthCaresScreen.Register -> AuthCaresScreen.Login
                        AuthCaresScreen.Info -> AuthCaresScreen.Welcome
                        AuthCaresScreen.Login -> AuthCaresScreen.Welcome

                        // Sub-pantallas que vuelven al Home
                        AuthCaresScreen.Alerts,
                        AuthCaresScreen.Kids,
                        AuthCaresScreen.Stats,
                        AuthCaresScreen.Settings,
                        AuthCaresScreen.HomeCentralized -> AuthCaresScreen.Home

                        // Sub-pantallas profundas
                        AuthCaresScreen.AlertDetail -> AuthCaresScreen.Alerts
                        AuthCaresScreen.ChildProfile -> AuthCaresScreen.Kids
                        AuthCaresScreen.SettingsAlerts,
                        AuthCaresScreen.SettingsWatch,
                        AuthCaresScreen.Share -> AuthCaresScreen.Settings

                        else -> AuthCaresScreen.Home // Fallback
                    }
                }

                // Manejo del botón físico de atrás
                BackHandler(enabled = currentScreen != AuthCaresScreen.Home) {
                    onBack()
                }

                when (currentScreen) {
                    // --- ONBOARDING & AUTH ---
                    AuthCaresScreen.Welcome -> {
                        BienvenidaAuthCaresScreen(
                            heroPainter = painterResource(R.drawable.hero_bienvenida_authcares),
                            onStartClick = { navigateTo(AuthCaresScreen.Info) }
                        )
                    }

                    AuthCaresScreen.Info -> {
                        InformacionAuthCaresScreen(
                            onContinueClick = { navigateTo(AuthCaresScreen.Login) }
                        )
                    }

                    AuthCaresScreen.Login -> {
                        IniciarSesionAuthCaresScreen(
                            email = loginEmail,
                            password = loginPassword,
                            onEmailChange = { loginEmail = it },
                            onPasswordChange = { loginPassword = it },
                            onLoginClick = { navigateTo(AuthCaresScreen.Home) },
                            onCreateAccountClick = { navigateTo(AuthCaresScreen.Register) }
                        )
                    }

                    AuthCaresScreen.Register -> {
                        CrearCuentaAuthCaresScreen(
                            fullName = registerFullName,
                            email = registerEmail,
                            password = registerPassword,
                            onFullNameChange = { registerFullName = it },
                            onEmailChange = { registerEmail = it },
                            onPasswordChange = { registerPassword = it },
                            onAlreadyHaveAccountClick = { navigateTo(AuthCaresScreen.Login) },
                            onRegisterClick = { navigateTo(AuthCaresScreen.Home) }
                        )
                    }

                    // --- MAIN APP SCREENS (CON NAVEGACIÓN ACTIVA) ---

                    AuthCaresScreen.Home -> {
                        InicioAuthCaresScreen(
                            onNavigateTo = { screen -> navigateTo(screen) }
                        )
                    }

                    AuthCaresScreen.HomeCentralized -> {
                        InicioCentralizadoScreen(
                            modifier = Modifier,
                            onNavigateTo = { screen -> navigateTo(screen) }
                        )
                    }

                    AuthCaresScreen.Alerts -> {
                        AlertasInteligentesScreen(
                            onNavigateTo = { screen -> navigateTo(screen) }
                        )
                    }

                    AuthCaresScreen.AlertDetail -> {
                        DetalleAlertaScreen(
                            onBackClick = onBack,
                            onMarkRevisedClick = { /* TODO Lógica */ },
                            onSharePsychologistClick = { navigateTo(AuthCaresScreen.Share) },
                            onCallSchoolClick = { /* TODO Lógica */ }
                        )
                    }

                    AuthCaresScreen.AI -> {
                        AsistenteIAScreen(
                            onBackClick = onBack
                        )
                    }

                    AuthCaresScreen.Kids -> {
                        InicioCentralizadoScreen(
                            modifier = Modifier,
                            onNavigateTo = { screen -> navigateTo(screen) }
                        )
                    }

                    AuthCaresScreen.ChildProfile -> {
                        PerfilDetalladoScreen(
                            onBackClick = onBack
                        )
                    }

                    AuthCaresScreen.Stats -> {
                        EstadisticasAuthCaresScreen(
                            onNavigateTo = { screen -> navigateTo(screen) }
                        )
                    }

                    AuthCaresScreen.Settings -> {
                        PerfilAuthCaresScreen(
                            onNavigateTo = { screen -> navigateTo(screen) }
                        )
                    }

                    AuthCaresScreen.SettingsAlerts -> {
                        ConfiguracionAlertasScreen(
                            onBackClick = onBack
                        )
                    }

                    AuthCaresScreen.SettingsWatch -> {
                        ConfiguracionRelojScreen(
                            onBackClick = onBack
                        )
                    }

                    AuthCaresScreen.Share -> {
                        CompartirAuthCaresScreen(
                            onBackClick = onBack
                        )
                    }
                }
            }
        }
    }
}

// Enumerado actualizado con todas tus pantallas
enum class AuthCaresScreen {
    Welcome,      // BienvenidaAuthCaresScreen
    Info,         // InformacionAuthCaresScreen
    Login,        // IniciarSesionAuthCaresScreen
    Register,     // CrearCuentaAuthCaresScreen

    // Main Tabs
    Home,         // InicioAuthCaresScreen
    HomeCentralized, // InicioCentralizadoScreen (Alternativa)
    Alerts,       // AlertasInteligentesScreen
    AI,           // AsistenteIAScreen
    Kids,         // NinosRegistradosScreen
    Stats,        // EstadisticasAuthCaresScreen
    Settings,     // PerfilAuthCaresScreen

    // Sub Screens
    AlertDetail,  // DetalleAlertaScreen
    ChildProfile, // PerfilDetalladoScreen
    SettingsAlerts, // ConfiguracionAlertasScreen
    SettingsWatch,   // ConfiguracionRelojScreen
    Share        // CompartirAuthCaresScreen
}
package com.choque.authcares2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.choque.authcares2.ui.screens.assistant.AsistenteIAScreen
import com.choque.authcares2.ui.screens.auth.BienvenidaAuthCaresScreen
import com.choque.authcares2.ui.screens.auth.CrearCuentaAuthCaresScreen
import com.choque.authcares2.ui.screens.auth.InformacionAuthCaresScreen
import com.choque.authcares2.ui.screens.auth.IniciarSesionAuthCaresScreen
import com.choque.authcares2.ui.screens.home.InicioAuthCaresScreen
import com.choque.authcares2.ui.screens.home.InicioCentralizadoScreen
import com.choque.authcares2.ui.screens.profile.PerfilAuthCaresScreen
import com.choque.authcares2.ui.screens.profile.PerfilDetalladoScreen
import com.choque.authcares2.ui.screens.alerts.AlertasInteligentesScreen
import com.choque.authcares2.ui.screens.alerts.DetalleAlertaScreen
import com.choque.authcares2.ui.screens.settings.ConfiguracionAlertasScreen
import com.choque.authcares2.ui.screens.settings.ConfiguracionRelojScreen
import com.choque.authcares2.ui.screens.share.CompartirAuthCaresScreen
import com.choque.authcares2.ui.screens.stats.EstadisticasAuthCaresScreen
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthCares2Theme {
                val authViewModel: AuthViewModel = viewModel()

                val startDestination = if (authViewModel.isUserLoggedIn()) {
                    AuthCaresScreen.Home
                } else {
                    AuthCaresScreen.Welcome
                }

                var currentScreen by remember { mutableStateOf(startDestination) }

                val navigateTo: (AuthCaresScreen) -> Unit = { screen ->
                    currentScreen = screen
                }

                val onBack: () -> Unit = {
                    currentScreen = when (currentScreen) {
                        AuthCaresScreen.Register -> AuthCaresScreen.Login
                        AuthCaresScreen.Info -> AuthCaresScreen.Welcome
                        AuthCaresScreen.Login -> AuthCaresScreen.Welcome
                        AuthCaresScreen.Alerts,
                        AuthCaresScreen.Kids,
                        AuthCaresScreen.Stats,
                        AuthCaresScreen.Settings,
                        AuthCaresScreen.HomeCentralized -> AuthCaresScreen.Home
                        AuthCaresScreen.AlertDetail -> AuthCaresScreen.Alerts
                        AuthCaresScreen.ChildProfile -> AuthCaresScreen.Kids
                        AuthCaresScreen.SettingsAlerts,
                        AuthCaresScreen.SettingsWatch,
                        AuthCaresScreen.Share -> AuthCaresScreen.Settings
                        else -> AuthCaresScreen.Home
                    }
                }

                BackHandler(enabled = currentScreen != AuthCaresScreen.Home) {
                    onBack()
                }

                when (currentScreen) {
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
                            email = authViewModel.loginEmail,
                            password = authViewModel.loginPassword,
                            onEmailChange = { authViewModel.loginEmail = it },
                            onPasswordChange = { authViewModel.loginPassword = it },
                            onLoginClick = {
                                authViewModel.login(
                                    onSuccess = { navigateTo(AuthCaresScreen.Home) },
                                    onError = { error -> println(error) }
                                )
                            },
                            onCreateAccountClick = { navigateTo(AuthCaresScreen.Register) }
                        )
                    }

                    AuthCaresScreen.Register -> {
                        CrearCuentaAuthCaresScreen(
                            fullName = authViewModel.registerFullName,
                            email = authViewModel.registerEmail,
                            password = authViewModel.registerPassword,
                            onFullNameChange = { authViewModel.registerFullName = it },
                            onEmailChange = { authViewModel.registerEmail = it },
                            onPasswordChange = { authViewModel.registerPassword = it },
                            onAlreadyHaveAccountClick = { navigateTo(AuthCaresScreen.Login) },
                            onRegisterClick = {
                                authViewModel.register(
                                    onSuccess = { navigateTo(AuthCaresScreen.Home) },
                                    onError = { error -> println(error) }
                                )
                            }
                        )
                    }

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
                            onMarkRevisedClick = { },
                            onSharePsychologistClick = { navigateTo(AuthCaresScreen.Share) },
                            onCallSchoolClick = { }
                        )
                    }

                    AuthCaresScreen.AI -> {
                        AsistenteIAScreen(onBackClick = onBack)
                    }

                    AuthCaresScreen.Kids -> {
                        InicioCentralizadoScreen(
                            modifier = Modifier,
                            onNavigateTo = { screen -> navigateTo(screen) }
                        )
                    }

                    AuthCaresScreen.ChildProfile -> {
                        PerfilDetalladoScreen(onBackClick = onBack)
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
                        ConfiguracionAlertasScreen(onBackClick = onBack)
                    }

                    AuthCaresScreen.SettingsWatch -> {
                        ConfiguracionRelojScreen(onBackClick = onBack)
                    }

                    AuthCaresScreen.Share -> {
                        CompartirAuthCaresScreen(onBackClick = onBack)
                    }
                }
            }
        }
    }
}

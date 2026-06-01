package com.choque.authcares2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.choque.authcares2.ui.components.HomeBottomBar
import com.choque.authcares2.ui.components.HomeTab
import com.choque.authcares2.ui.components.HomeTopBar
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
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val authViewModel: AuthViewModel = viewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val loginState by authViewModel.loginState.collectAsState()
    val registerState by authViewModel.registerState.collectAsState()

    LaunchedEffect(loginState.isSuccess) {
        if (loginState.isSuccess) {
            navController.navigate(AuthCaresScreen.Home.name) {
                popUpTo(AuthCaresScreen.Welcome.name) { inclusive = true }
            }
        }
    }

    LaunchedEffect(registerState.isSuccess) {
        if (registerState.isSuccess) {
            navController.navigate(AuthCaresScreen.Home.name) {
                popUpTo(AuthCaresScreen.Welcome.name) { inclusive = true }
            }
        }
    }

    val startDestination = if (authViewModel.isUserLoggedIn()) {
        AuthCaresScreen.Home.name
    } else {
        AuthCaresScreen.Welcome.name
    }

    // Pestañas principales que comparten el TopBar y BottomBar GLOBAL
    val mainTabs = listOf(
        AuthCaresScreen.Home.name,
        AuthCaresScreen.Stats.name,
        AuthCaresScreen.Kids.name,
        AuthCaresScreen.Settings.name
    )

    Scaffold(
        topBar = {
            // Solo mostramos el TopBar global en las pestañas principales
            if (currentRoute in mainTabs) {
                HomeTopBar(
                    onNavigateTo = { screen -> navController.navigate(screen.name) },
                    onBackClick = null // No hay botón atrás en las pestañas principales
                )
            }
        },
        bottomBar = {
            // Solo mostramos el BottomBar global en las pestañas principales
            if (currentRoute in mainTabs) {
                val selectedTab = when (currentRoute) {
                    AuthCaresScreen.Home.name -> HomeTab.Inicio
                    AuthCaresScreen.Stats.name -> HomeTab.Horarios
                    AuthCaresScreen.Kids.name -> HomeTab.Ninos
                    AuthCaresScreen.Settings.name -> HomeTab.Ajustes
                    else -> HomeTab.Inicio
                }
                HomeBottomBar(
                    selectedTab = selectedTab,
                    onTabClick = { tab ->
                        val route = when (tab) {
                            HomeTab.Inicio -> AuthCaresScreen.Home.name
                            HomeTab.Horarios -> AuthCaresScreen.Stats.name
                            HomeTab.Ninos -> AuthCaresScreen.Kids.name
                            HomeTab.Ajustes -> AuthCaresScreen.Settings.name
                        }
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(AuthCaresScreen.Home.name) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AuthCaresScreen.Welcome.name) {
                BienvenidaAuthCaresScreen(
                    heroPainter = painterResource(R.drawable.hero_bienvenida_authcares),
                    onStartClick = { navController.navigate(AuthCaresScreen.Info.name) }
                )
            }
            composable(AuthCaresScreen.Info.name) {
                InformacionAuthCaresScreen(
                    onContinueClick = { navController.navigate(AuthCaresScreen.Login.name) }
                )
            }
            composable(AuthCaresScreen.Login.name) {
                IniciarSesionAuthCaresScreen(
                    email = loginState.email,
                    password = loginState.password,
                    errorMessage = loginState.errorMessage,
                    isLoading = loginState.isLoading,
                    onEmailChange = { authViewModel.onLoginEmailChange(it) },
                    onPasswordChange = { authViewModel.onLoginPasswordChange(it) },
                    onLoginClick = { authViewModel.login() },
                    onCreateAccountClick = { navController.navigate(AuthCaresScreen.Register.name) }
                )
            }
            composable(AuthCaresScreen.Register.name) {
                CrearCuentaAuthCaresScreen(
                    fullName = registerState.fullName,
                    email = registerState.email,
                    password = registerState.password,
                    errorMessage = registerState.errorMessage,
                    isLoading = registerState.isLoading,
                    onFullNameChange = { authViewModel.onRegisterFullNameChange(it) },
                    onEmailChange = { authViewModel.onRegisterEmailChange(it) },
                    onPasswordChange = { authViewModel.onRegisterPasswordChange(it) },
                    onAlreadyHaveAccountClick = { navController.popBackStack() },
                    onRegisterClick = { authViewModel.register() }
                )
            }
            composable(AuthCaresScreen.Home.name) {
                InicioAuthCaresScreen(
                    onNavigateTo = { screen -> navController.navigate(screen.name) }
                )
            }
            composable(AuthCaresScreen.Stats.name) {
                EstadisticasAuthCaresScreen(
                    onNavigateTo = { screen -> navController.navigate(screen.name) }
                )
            }
            composable(AuthCaresScreen.Kids.name) {
                InicioCentralizadoScreen(
                    onNavigateTo = { screen -> navController.navigate(screen.name) }
                )
            }
            composable(AuthCaresScreen.Settings.name) {
                PerfilAuthCaresScreen(
                    onNavigateTo = { screen -> navController.navigate(screen.name) },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(AuthCaresScreen.Welcome.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(AuthCaresScreen.Alerts.name) {
                AlertasInteligentesScreen(
                    onNavigateTo = { screen -> navController.navigate(screen.name) }
                )
            }
            composable(AuthCaresScreen.AlertDetail.name) {
                DetalleAlertaScreen(
                    onBackClick = { navController.popBackStack() },
                    onMarkRevisedClick = { },
                    onSharePsychologistClick = { navController.navigate(AuthCaresScreen.Share.name) },
                    onCallSchoolClick = { }
                )
            }
            composable(AuthCaresScreen.AI.name) {
                AsistenteIAScreen(onBackClick = { navController.popBackStack() })
            }
            composable(AuthCaresScreen.ChildProfile.name) {
                PerfilDetalladoScreen(onBackClick = { navController.popBackStack() })
            }
            composable(AuthCaresScreen.SettingsAlerts.name) {
                ConfiguracionAlertasScreen(onBackClick = { navController.popBackStack() })
            }
            composable(AuthCaresScreen.SettingsWatch.name) {
                ConfiguracionRelojScreen(onBackClick = { navController.popBackStack() })
            }
            composable(AuthCaresScreen.Share.name) {
                CompartirAuthCaresScreen(onBackClick = { navController.popBackStack() })
            }
            composable(AuthCaresScreen.HomeCentralized.name) {
                InicioCentralizadoScreen(
                    onNavigateTo = { screen -> navController.navigate(screen.name) }
                )
            }
        }
    }
}

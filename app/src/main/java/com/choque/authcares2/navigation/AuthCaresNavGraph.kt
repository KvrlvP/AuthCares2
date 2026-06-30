package com.choque.authcares2.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.choque.authcares2.core.model.SensorUiState
import com.choque.authcares2.features.alerts.AlertsViewModel
import com.choque.authcares2.features.alerts.ui.AlertasInteligentesScreen
import com.choque.authcares2.features.alerts.ui.DetalleAlertaScreen
import com.choque.authcares2.features.assistant.ui.AsistenteIAScreen
import com.choque.authcares2.features.auth.AuthViewModel
import com.choque.authcares2.features.auth.LoginState
import com.choque.authcares2.features.auth.ui.AuthOnboardingPagerScreen
import com.choque.authcares2.features.auth.ui.SplashVerificacionScreen
import com.choque.authcares2.features.home.ui.InicioAuthCaresScreen
import com.choque.authcares2.features.home.ui.NinosRegistradosScreen
import com.choque.authcares2.features.monitoring.SensorViewModel
import com.choque.authcares2.features.profile.ui.PerfilAuthCaresScreen
import com.choque.authcares2.features.profile.ui.PerfilDetalladoScreen
import com.choque.authcares2.features.settings.ui.ConfiguracionAlertasScreen
import com.choque.authcares2.features.settings.ui.ConfiguracionRelojScreen
import com.choque.authcares2.features.settings.ui.NingunRelojConectadoScreen
import com.choque.authcares2.features.share.ui.CompartirAuthCaresScreen
import com.choque.authcares2.features.stats.ui.EstadisticasAuthCaresScreen
import androidx.compose.ui.platform.LocalContext
import com.choque.authcares2.features.alerts.service.SensorMonitoringService

fun NavGraphBuilder.authCaresGraph(
    navController: NavHostController,
    loginState: LoginState,
    sensorState: SensorUiState,
    userName: String,
    userFullName: String,
    userEmail: String,
    authViewModel: AuthViewModel,
    sensorViewModel: SensorViewModel,
    alertsViewModel: AlertsViewModel,
    onGoogleLogin: () -> Unit
) {
    composable(AuthCaresScreen.Welcome.name) {
        AuthOnboardingPagerScreen(
            email = loginState.email,
            password = loginState.password,
            errorMessage = loginState.errorMessage,
            isLoading = loginState.isLoading,
            onEmailChange = { authViewModel.onLoginEmailChange(it) },
            onPasswordChange = { authViewModel.onLoginPasswordChange(it) },
            onLoginClick = { authViewModel.login() },
            onGoogleLoginClick = {
                authViewModel.setLoading(true)
                onGoogleLogin()
            }
        )
    }
    composable(AuthCaresScreen.Splash.name) {
        SplashVerificacionScreen()
    }
    composable(AuthCaresScreen.Home.name) {
        InicioAuthCaresScreen(
            userName = userName,
            sensorState = sensorState,
            onNavigateTo = { screen -> navController.navigate(screen.name) },
            onChildClick = { child ->
                sensorViewModel.selectChild(child)
                navController.navigate(AuthCaresScreen.ChildProfile.name)
            }
        )
    }
    composable(AuthCaresScreen.Stats.name) {
        EstadisticasAuthCaresScreen(
            childName = sensorState.childName.ifBlank { "tu ni�o" },
            watchId = sensorState.relojId,
            onNavigateTo = { screen -> navController.navigate(screen.name) }
        )
    }
    composable(AuthCaresScreen.Kids.name) {
        NinosRegistradosScreen(
            children = sensorState.registeredChildren,
            onNavigateTo = { screen -> navController.navigate(screen.name) },
            onChildClick = { child ->
                sensorViewModel.selectChild(child)
                navController.navigate(AuthCaresScreen.ChildProfile.name)
            }
        )
    }
    composable(AuthCaresScreen.Settings.name) {
        PerfilAuthCaresScreen(
            userName = userFullName,
            userEmail = userEmail,
            childName = sensorState.childName,
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
        val alertsState by alertsViewModel.uiState.collectAsState()

        LaunchedEffect(sensorState.relojId, sensorState.childName) {
            alertsViewModel.observeWatch(
                watchId = sensorState.relojId,
                childName = sensorState.childName.ifBlank { "tu ni�o" }
            )
        }

        AlertasInteligentesScreen(
            alerts = alertsState.alerts,
            isLoading = alertsState.isLoading,
            errorMessage = alertsState.errorMessage,
            onBackClick = { navController.popBackStack() },
            onAlertClick = { alert ->
                alertsViewModel.selectAlert(alert)
                navController.navigate(AuthCaresScreen.AlertDetail.name)
            }
        )
    }
    composable(AuthCaresScreen.AlertDetail.name) {
        val alertsState by alertsViewModel.uiState.collectAsState()

        DetalleAlertaScreen(
            alert = alertsState.selectedAlert,
            onBackClick = { navController.popBackStack() },
            onMarkRevisedClick = { navController.popBackStack() },
            onSharePsychologistClick = { navController.navigate(AuthCaresScreen.Share.name) },
            onCallSchoolClick = { }
        )
    }
    composable(AuthCaresScreen.AI.name) {
        AsistenteIAScreen(
            sensorState = sensorState,
            onBackClick = { navController.popBackStack() }
        )
    }
    composable(AuthCaresScreen.ChildProfile.name) {
        PerfilDetalladoScreen(
            child = sensorState.selectedChild,
            onBackClick = { navController.popBackStack() },
            onNavigateTo = { screen -> navController.navigate(screen.name) }
        )
    }
    composable(AuthCaresScreen.SettingsAlerts.name) {
        ConfiguracionAlertasScreen(onBackClick = { navController.popBackStack() })
    }
    composable(AuthCaresScreen.SettingsWatch.name) {
        ConfiguracionRelojScreen(onBackClick = { navController.popBackStack() })
    }
    composable(AuthCaresScreen.NoWatchConnected.name) {
        val context = LocalContext.current

        LaunchedEffect(
            sensorState.watchConnected,
            sensorState.connectedWatchId
        ) {
            if (sensorState.watchConnected) {
                val watchId = sensorState.connectedWatchId

                if (!watchId.isNullOrBlank()) {
                    SensorMonitoringService.start(
                        context = context,
                        watchId = watchId
                    )

                    sensorViewModel.loadChildAndWatch()
                    sensorViewModel.onWatchConnectionHandled()

                    navController.navigate(AuthCaresScreen.Home.name) {
                        popUpTo(AuthCaresScreen.NoWatchConnected.name) {
                            inclusive = true
                        }
                    }
                }
            }
        }

        NingunRelojConectadoScreen(
            watchCode = sensorState.watchCodeInput,
            errorMessage = sensorState.errorMessage,
            isConnecting = sensorState.isConnecting,
            onWatchCodeChange = { sensorViewModel.onWatchCodeChange(it) },
            onConnectClick = { sensorViewModel.connectWatch() }
        )
    }
    composable(AuthCaresScreen.Share.name) {
        CompartirAuthCaresScreen(onBackClick = { navController.popBackStack() })
    }
}

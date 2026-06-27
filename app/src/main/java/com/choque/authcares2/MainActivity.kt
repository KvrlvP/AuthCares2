package com.choque.authcares2

import com.choque.authcares2.navigation.AuthCaresScreen

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.choque.authcares2.ui.components.HomeBottomBar
import com.choque.authcares2.ui.components.HomeTab
import com.choque.authcares2.ui.components.HomeTopBar
import com.choque.authcares2.ui.screens.assistant.AsistenteIAScreen
import com.choque.authcares2.features.auth.ui.BienvenidaAuthCaresScreen
import com.choque.authcares2.features.auth.ui.InformacionAuthCaresScreen
import com.choque.authcares2.features.auth.ui.IniciarSesionAuthCaresScreen
import com.choque.authcares2.features.auth.ui.SplashVerificacionScreen
import com.choque.authcares2.ui.screens.home.InicioAuthCaresScreen
import com.choque.authcares2.ui.screens.home.InicioCentralizadoScreen
import com.choque.authcares2.ui.screens.home.NinosRegistradosScreen
import com.choque.authcares2.ui.screens.profile.PerfilAuthCaresScreen
import com.choque.authcares2.ui.screens.profile.PerfilDetalladoScreen
import com.choque.authcares2.ui.screens.alerts.AlertasInteligentesScreen
import com.choque.authcares2.ui.screens.alerts.DetalleAlertaScreen
import com.choque.authcares2.ui.screens.settings.ConfiguracionAlertasScreen
import com.choque.authcares2.ui.screens.settings.ConfiguracionRelojScreen
import com.choque.authcares2.ui.screens.settings.NingunRelojConectadoScreen
import com.choque.authcares2.ui.screens.share.CompartirAuthCaresScreen
import com.choque.authcares2.ui.screens.stats.EstadisticasAuthCaresScreen
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.alerts.CriticalAlertNotifier
import androidx.core.content.ContextCompat
import com.choque.authcares2.features.auth.AuthViewModel
import com.choque.authcares2.viewmodels.SensorViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.choque.authcares2.features.auth.ui.AuthOnboardingPagerScreen

class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) showFullScreenPermissionExplanationIfNeeded()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CriticalAlertNotifier.createNotificationChannel(this)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            AuthCares2Theme {
                MainApp()
            }
        }
        requestCriticalAlertPermissions()
    }

    private fun requestCriticalAlertPermissions() {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            showFullScreenPermissionExplanationIfNeeded()
        }
    }

    private fun showFullScreenPermissionExplanationIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return

        val notificationManager = getSystemService(NotificationManager::class.java)
        if (notificationManager.canUseFullScreenIntent()) return

        val preferences = getSharedPreferences("critical_alert_permissions", MODE_PRIVATE)
        if (preferences.getBoolean("full_screen_explanation_shown", false)) return
        preferences.edit().putBoolean("full_screen_explanation_shown", true).apply()

        AlertDialog.Builder(this)
            .setTitle("Permitir alertas críticas")
            .setMessage(
                "Para mostrar una emergencia sobre la pantalla bloqueada o sobre otras apps, " +
                    "activa el permiso de alertas en pantalla completa para AuthCares."
            )
            .setNegativeButton("Ahora no", null)
            .setPositiveButton("Abrir configuración") { _, _ ->
                startActivity(
                    Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                        data = Uri.parse("package:$packageName")
                    }
                )
            }
            .show()
    }
}

@Composable
fun MainApp() {
    val authViewModel: AuthViewModel = viewModel()
    val sensorViewModel: SensorViewModel = viewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val loginState by authViewModel.loginState.collectAsState()
    val sensorState by sensorViewModel.sensorState.collectAsState()
    val userName by authViewModel.userName.collectAsState()
    val userFullName by authViewModel.userFullName.collectAsState()
    val userEmail by authViewModel.userEmail.collectAsState()

    // Configuración de Google Sign-In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(stringResource(id = R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { token ->
                authViewModel.signInWithGoogle(token)
            } ?: run {
                authViewModel.setLoading(false)
            }
        } catch (e: ApiException) {
            authViewModel.setLoading(false)
            e.printStackTrace()
        }
    }

    LaunchedEffect(loginState.isSuccess) {
        if (loginState.isSuccess) {
            sensorViewModel.loadChildAndWatch()
            navController.navigate(AuthCaresScreen.Splash.name) {
                popUpTo(AuthCaresScreen.Welcome.name) { inclusive = true }
            }
        }
    }

    LaunchedEffect(currentRoute, sensorState.isLoading) {
        if (currentRoute == AuthCaresScreen.Splash.name && !sensorState.isLoading) {
            val destination = if (sensorState.relojId == null) {
                AuthCaresScreen.NoWatchConnected.name
            } else {
                AuthCaresScreen.Home.name
            }
            navController.navigate(destination) {
                popUpTo(AuthCaresScreen.Splash.name) { inclusive = true }
            }
        }
    }

    LaunchedEffect(currentRoute, sensorState.isLoading, sensorState.relojId) {
        if (
            currentRoute == AuthCaresScreen.Home.name &&
            !sensorState.isLoading &&
            sensorState.relojId == null
        ) {
            navController.navigate(AuthCaresScreen.NoWatchConnected.name)
        }
        
        if (
            currentRoute == AuthCaresScreen.NoWatchConnected.name &&
            sensorState.relojId != null
        ) {
            navController.navigate(AuthCaresScreen.Home.name) {
                popUpTo(AuthCaresScreen.NoWatchConnected.name) { inclusive = true }
            }
        }
    }

    val startDestination = if (authViewModel.isUserLoggedIn()) {
        AuthCaresScreen.Home.name
    } else {
        AuthCaresScreen.Welcome.name
    }

    val mainTabs = listOf(
        AuthCaresScreen.Home.name,
        AuthCaresScreen.Stats.name,
        AuthCaresScreen.Kids.name,
        AuthCaresScreen.Settings.name
    )

    Scaffold(
        topBar = {
            if (currentRoute in mainTabs) {
                HomeTopBar(
                    onNavigateTo = { screen -> navController.navigate(screen.name) },
                    onBackClick = null
                )
            }
        },
        bottomBar = {
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
                        googleLauncher.launch(googleSignInClient.signInIntent)
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
                    childName = sensorState.childName.ifBlank { "tu niño" },
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
            composable(AuthCaresScreen.HomeCentralized.name) {
                InicioCentralizadoScreen(
                    userName = userName,
                    childName = sensorState.childName.ifBlank { "tu niño" },
                    onNavigateTo = { screen -> navController.navigate(screen.name) }
                )
            }
        }
    }
}

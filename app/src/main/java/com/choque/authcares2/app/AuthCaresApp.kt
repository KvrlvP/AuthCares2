package com.choque.authcares2.app

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.choque.authcares2.R
import com.choque.authcares2.features.auth.AuthViewModel
import com.choque.authcares2.features.monitoring.SensorViewModel
import com.choque.authcares2.navigation.AuthCaresScreen
import com.choque.authcares2.navigation.authCaresGraph
import com.choque.authcares2.ui.components.HomeBottomBar
import com.choque.authcares2.ui.components.HomeTab
import com.choque.authcares2.ui.components.HomeTopBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
@Composable
fun AuthCaresApp() {
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
            authCaresGraph(
                navController = navController,
                loginState = loginState,
                sensorState = sensorState,
                userName = userName,
                userFullName = userFullName,
                userEmail = userEmail,
                authViewModel = authViewModel,
                sensorViewModel = sensorViewModel,
                onGoogleLogin = {
                    authViewModel.setLoading(true)
                    googleLauncher.launch(googleSignInClient.signInIntent)
                }
            )        }
    }
}


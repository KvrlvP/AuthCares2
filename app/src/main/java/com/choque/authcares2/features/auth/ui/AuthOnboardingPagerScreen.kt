package com.choque.authcares2.features.auth.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.choque.authcares2.R
import kotlinx.coroutines.launch

@Composable
fun AuthOnboardingPagerScreen(
    email: String = "",
    password: String = "",
    errorMessage: String? = null,
    isLoading: Boolean = false,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        userScrollEnabled = true
    ) { page ->
        when (page) {
            0 -> {
                BienvenidaAuthCaresScreen(
                    heroPainter = painterResource(id = R.drawable.hero_bienvenida_authcares),
                    currentPage = page,
                    onStartClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }
            1 -> {
                InformacionAuthCaresScreen(
                    currentPage = page,
                    onContinueClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    }
                )
            }
            2 -> {
                IniciarSesionAuthCaresScreen(
                    email = email,
                    password = password,
                    errorMessage = errorMessage,
                    isLoading = isLoading,
                    currentPage = page,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onLoginClick = onLoginClick,
                    onGoogleLoginClick = onGoogleLoginClick
                )
            }
        }
    }
}
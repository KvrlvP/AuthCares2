package com.choque.authcares2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.choque.authcares2.ui.screens.auth.CrearCuentaAuthCaresScreen
import com.choque.authcares2.ui.screens.auth.IniciarSesionAuthCaresScreen
import com.choque.authcares2.ui.screens.onboarding.BienvenidaAuthCaresScreen
import com.choque.authcares2.ui.theme.AuthCares2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthCares2Theme {
                var currentScreen by remember { mutableStateOf(AuthCaresScreen.Welcome) }
                var loginEmail by remember { mutableStateOf("") }
                var loginPassword by remember { mutableStateOf("") }
                var registerFullName by remember { mutableStateOf("") }
                var registerEmail by remember { mutableStateOf("") }
                var registerPassword by remember { mutableStateOf("") }

                when (currentScreen) {
                    AuthCaresScreen.Welcome -> {
                        BienvenidaAuthCaresScreen(
                            heroPainter = painterResource(R.drawable.hero_bienvenida_authcares),
                            onStartClick = { currentScreen = AuthCaresScreen.Login }
                        )
                    }

                    AuthCaresScreen.Login -> {
                        IniciarSesionAuthCaresScreen(
                            email = loginEmail,
                            password = loginPassword,
                            onEmailChange = { loginEmail = it },
                            onPasswordChange = { loginPassword = it },
                            onCreateAccountClick = { currentScreen = AuthCaresScreen.Register }
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
                            onAlreadyHaveAccountClick = { currentScreen = AuthCaresScreen.Login }
                        )
                    }
                }
            }
        }
    }
}

private enum class AuthCaresScreen {
    Welcome,
    Login,
    Register
}

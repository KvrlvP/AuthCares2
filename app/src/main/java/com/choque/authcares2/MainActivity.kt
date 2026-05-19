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
import com.choque.authcares2.ui.screens.auth.IniciarSesionAuthCaresScreen
import com.choque.authcares2.ui.screens.onboarding.BienvenidaAuthCaresScreen
import com.choque.authcares2.ui.theme.AuthCares2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthCares2Theme {
                var showLogin by remember { mutableStateOf(false) }
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                if (showLogin) {
                    IniciarSesionAuthCaresScreen(
                        email = email,
                        password = password,
                        onEmailChange = { email = it },
                        onPasswordChange = { password = it }
                    )
                } else {
                    BienvenidaAuthCaresScreen(
                        heroPainter = painterResource(R.drawable.hero_bienvenida_authcares),
                        onStartClick = { showLogin = true }
                    )
                }
            }
        }
    }
}

package com.choque.authcares2.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choque.authcares2.R
import com.choque.authcares2.ui.theme.AuthCares2Theme
import com.choque.authcares2.ui.theme.AuthCaresOnPrimary
import com.choque.authcares2.ui.theme.AuthCaresOnSurface
import com.choque.authcares2.ui.theme.AuthCaresOnSurfaceVariant
import com.choque.authcares2.ui.theme.AuthCaresOutlineVariant
import com.choque.authcares2.ui.theme.AuthCaresPrimary
import com.choque.authcares2.ui.theme.AuthCaresSecondary
import com.choque.authcares2.ui.theme.AuthCaresSurface
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainer
import com.choque.authcares2.ui.theme.AuthCaresSurfaceContainerLow
import com.choque.authcares2.ui.theme.AuthCaresWhiteSurface
import com.choque.authcares2.ui.components.OnboardingDots
import com.choque.authcares2.ui.components.GoogleSignInButton

@Composable
fun CrearCuentaAuthCaresScreen(
    fullName: String = "",
    email: String = "",
    password: String = "",
    errorMessage: String? = null,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    onFullNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onGoogleRegisterClick: () -> Unit = {},
    onAlreadyHaveAccountClick: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AuthCaresSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterHeader()

            Spacer(modifier = Modifier.height(32.dp))

            OnboardingDots(currentPage = 2, pageCount = 3)

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                RegisterTextField(
                    label = "Nombre completo",
                    value = fullName,
                    placeholder = "Ej. María García",
                    enabled = !isLoading,
                    keyboardType = KeyboardType.Text,
                    onValueChange = onFullNameChange
                )

                RegisterTextField(
                    label = "Correo",
                    value = email,
                    placeholder = "ejemplo@correo.com",
                    enabled = !isLoading,
                    keyboardType = KeyboardType.Email,
                    onValueChange = onEmailChange
                )

                RegisterTextField(
                    label = "Contraseña",
                    value = password,
                    placeholder = "Mínimo 8 caracteres",
                    enabled = !isLoading,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_authcares_visibility),
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = if (passwordVisible) AuthCaresPrimary else AuthCaresOutlineVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    onValueChange = onPasswordChange
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                SecurityMessage()
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRegisterClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuthCaresPrimary,
                        contentColor = AuthCaresOnPrimary
                    )
                ) {
                    Text(
                        text = if (isLoading) "Registrando..." else "Registrarse",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                GoogleSignInButton(
                    onClick = onGoogleRegisterClick,
                    text = "Registrarse con Google"
                )

                TextButton(
                    onClick = onAlreadyHaveAccountClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AuthCaresPrimary
                    )
                ) {
                    Text(
                        text = "Ya tengo cuenta",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AuthCares",
            color = AuthCaresPrimary,
            fontSize = 34.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Crear cuenta",
            color = AuthCaresOnSurface,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RegisterTextField(
    label: String,
    value: String,
    placeholder: String,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = AuthCaresOnSurfaceVariant,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            enabled = enabled,
            placeholder = {
                Text(
                    text = placeholder,
                    color = AuthCaresOnSurface,
                    fontSize = 18.sp
                )
            },
            trailingIcon = trailingIcon,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AuthCaresOnSurface,
                unfocusedTextColor = AuthCaresOnSurface,
                focusedContainerColor = AuthCaresWhiteSurface,
                unfocusedContainerColor = AuthCaresWhiteSurface,
                focusedBorderColor = AuthCaresSecondary,
                unfocusedBorderColor = AuthCaresOutlineVariant,
                cursorColor = AuthCaresPrimary
            )
        )
    }
}

@Composable
private fun SecurityMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AuthCaresSurfaceContainerLow)
            .border(
                width = 1.dp,
                color = AuthCaresSurfaceContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_authcares_lock),
                contentDescription = null,
                tint = AuthCaresSecondary,
                modifier = Modifier.size(22.dp)
            )

            Text(
                text = "Tu información se guarda de forma segura.",
                color = AuthCaresOnSurfaceVariant,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CrearCuentaAuthCaresScreenPreview() {
    AuthCares2Theme {
        CrearCuentaAuthCaresScreen()
    }
}

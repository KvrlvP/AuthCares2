package com.choque.authcares2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserName(): String {
        return auth.currentUser?.displayName?.split(" ")?.firstOrNull() ?: "Usuario"
    }

    fun onLoginEmailChange(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    fun onLoginPasswordChange(password: String) {
        _loginState.update { it.copy(password = password) }
    }

    fun onRegisterFullNameChange(fullName: String) {
        _registerState.update { it.copy(fullName = fullName) }
    }

    fun onRegisterEmailChange(email: String) {
        _registerState.update { it.copy(email = email) }
    }

    fun onRegisterPasswordChange(password: String) {
        _registerState.update { it.copy(password = password) }
    }

    fun login() {
        val state = _loginState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _loginState.update { it.copy(errorMessage = "Correo o contraseña incorrectos") }
            return
        }

        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                auth.signInWithEmailAndPassword(state.email, state.password).await()
                _loginState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: FirebaseAuthInvalidUserException) {
                _loginState.update { it.copy(isLoading = false, errorMessage = "Correo o contraseña incorrectos") }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _loginState.update { it.copy(isLoading = false, errorMessage = "Correo o contraseña incorrectos") }
            } catch (e: Exception) {
                _loginState.update { it.copy(isLoading = false, errorMessage = "Error inesperado. Inténtalo de nuevo.") }
            }
        }
    }

    fun register() {
        val state = _registerState.value
        // Validaciones locales
        val error = validateRegister(state.email, state.password)
        if (error != null) {
            _registerState.update { it.copy(errorMessage = error) }
            return
        }

        viewModelScope.launch {
            _registerState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = auth.createUserWithEmailAndPassword(state.email, state.password).await()
                
                // Guardar el nombre completo en el perfil de Firebase
                val profileUpdates = userProfileChangeRequest {
                    displayName = state.fullName
                }
                result.user?.updateProfile(profileUpdates)?.await()

                _registerState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: FirebaseAuthUserCollisionException) {
                _registerState.update { it.copy(isLoading = false, errorMessage = "Este correo ya se encuentra registrado.") }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _registerState.update { it.copy(isLoading = false, errorMessage = "Por favor, ingresa un correo electrónico válido.") }
            } catch (e: Exception) {
                _registerState.update { it.copy(isLoading = false, errorMessage = "Error al crear la cuenta. Inténtalo de nuevo.") }
            }
        }
    }

    private fun validateRegister(email: String, password: String): String? {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Por favor, ingresa un correo electrónico válido."
        }
        if (password.length < 8) {
            return "La contraseña debe tener al menos 8 caracteres."
        }
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }

        if (!hasLetter || !hasDigit || !hasSpecial) {
            return "La contraseña debe incluir al menos una letra, un número y un símbolo especial (ej. ?, !, @, $)."
        }
        return null
    }

    fun clearErrors() {
        _loginState.update { it.copy(errorMessage = null) }
        _registerState.update { it.copy(errorMessage = null) }
    }

    fun logout() {
        auth.signOut()
        _loginState.update { LoginState() }
        _registerState.update { RegisterState() }
    }
}

package com.choque.authcares2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")
    var registerFullName by mutableStateOf("")
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var authMessage by mutableStateOf<String?>(null)

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (loginEmail.isBlank() || loginPassword.isBlank()) {
            showError("Por favor completa email y contraseña", onError)
            return
        }

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(loginEmail, loginPassword).await()
                authMessage = null
                onSuccess()
            } catch (e: FirebaseAuthException) {
                showError("No se pudo iniciar sesión", onError)
            }
        }
    }

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (registerFullName.isBlank() || registerEmail.isBlank() || registerPassword.isBlank()) {
            showError("Todos los campos son obligatorios", onError)
            return
        }

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(registerEmail, registerPassword).await()
                authMessage = null
                onSuccess()
            } catch (e: FirebaseAuthException) {
                showError("No se pudo crear la cuenta", onError)
            }
        }
    }

    private fun showError(message: String, onError: (String) -> Unit) {
        authMessage = message
        onError(message)
    }
}

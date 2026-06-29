package com.choque.authcares2.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
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
    val isSuccess: Boolean = false,
    val userName: String = "Usuario"
)

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    private val _userName = MutableStateFlow("Usuario")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userFullName = MutableStateFlow("Usuario")
    val userFullName: StateFlow<String> = _userFullName.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    init {
        loadUserName()
    }

    fun loadUserName() {
        val user = auth.currentUser
        if (user == null) {
            _userName.value = "Usuario"
            _userFullName.value = "Usuario"
            _userEmail.value = ""
            return
        }

        _userEmail.value = user.email ?: ""

        // 1. Intentar desde el perfil de Auth (rápido)
        val profileName = user.displayName
        if (!profileName.isNullOrBlank()) {
            _userFullName.value = profileName
            _userName.value = profileName.split(" ").firstOrNull() ?: profileName
        }

        // 2. Respaldo desde Firestore (infalible)
        viewModelScope.launch {
            try {
                val doc = firestore.collection("usuarios").document(user.uid).get().await()
                val firestoreName = doc.getString("nombre")
                if (!firestoreName.isNullOrBlank()) {
                    _userFullName.value = firestoreName
                    _userName.value = firestoreName.split(" ").firstOrNull() ?: firestoreName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onLoginEmailChange(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    fun onLoginPasswordChange(password: String) {
        _loginState.update { it.copy(password = password) }
    }

    fun setLoading(isLoading: Boolean) {
        _loginState.update { it.copy(isLoading = isLoading) }
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
                val result = auth.signInWithEmailAndPassword(state.email, state.password).await()
                result.user?.let { user ->
                    saveUserToFirestore(
                        uid = user.uid,
                        email = user.email ?: "",
                        name = user.displayName ?: "Usuario"
                    )
                    loadUserName()
                }
                _loginState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: FirebaseAuthInvalidUserException) {
                _loginState.update { it.copy(isLoading = false, errorMessage = "Correo o contraseña incorrectos") }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _loginState.update { it.copy(isLoading = false, errorMessage = "Correo o contraseña incorrectos") }
            } catch (e: Exception) {
                _loginState.update { it.copy(isLoading = false, errorMessage = "Error inesperado: ${e.localizedMessage}") }
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                result.user?.let { user ->
                    saveUserToFirestore(
                        uid = user.uid,
                        email = user.email ?: "",
                        name = user.displayName ?: "Usuario de Google"
                    )
                    loadUserName()
                }
                _loginState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _loginState.update { it.copy(isLoading = false, errorMessage = "Error con Google: ${e.localizedMessage}") }
            }
        }
    }

    private suspend fun saveUserToFirestore(uid: String, email: String, name: String) {
        try {
            val userData = hashMapOf(
                "uid" to uid,
                "email" to email,
                "nombre" to name,
                "creadoEn" to Timestamp.now()
            )
            firestore.collection("usuarios").document(uid).set(userData).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearErrors() {
        _loginState.update { it.copy(errorMessage = null) }
    }

    fun logout() {
        auth.signOut()
        _loginState.update { LoginState() }
    }
}

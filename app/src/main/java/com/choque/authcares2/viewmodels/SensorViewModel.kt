package com.choque.authcares2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

data class ChildInfo(
    val id: String = "",
    val name: String = "",
    val relojId: String? = null,
    val nivelTea: String? = null,
    val fechaNacimiento: String? = null,
    val avatarRes: Int? = null
)

data class SensorUiState(
    val childName: String = "",
    val relojId: String? = null,
    val heartRate: Int? = null,
    val movement: String = "Sin datos",
    val status: String = "Cargando...",
    val lastSync: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val watchCodeInput: String = "",
    val isConnecting: Boolean = false,
    val registeredChildren: List<ChildInfo> = emptyList()
)

class SensorViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val realtime = FirebaseDatabase.getInstance()

    private val _sensorState = MutableStateFlow(SensorUiState())
    val sensorState: StateFlow<SensorUiState> = _sensorState.asStateFlow()

    private var latestListener: ValueEventListener? = null
    private var currentWatchId: String? = null

    init {
        loadChildAndWatch()
    }

    fun loadChildAndWatch() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _sensorState.update {
                it.copy(
                    isLoading = false,
                    status = "Inicia sesion",
                    errorMessage = "Necesitas iniciar sesion para ver los sensores."
                )
            }
            return
        }

        viewModelScope.launch {
            _sensorState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val childrenDocs = findAllChildrenForUser(uid)
                val childrenList = childrenDocs.map { doc ->
                    ChildInfo(
                        id = doc.id,
                        name = doc.getString("nombre").orEmpty(),
                        relojId = doc.getString("relojId"),
                        nivelTea = doc.getString("nivelTEA"),
                        fechaNacimiento = doc.getString("fechaNacimiento")
                    )
                }

                // El niño "activo" para los sensores es el primero con reloj vinculado
                val activeChild = childrenList.firstOrNull { !it.relojId.isNullOrBlank() }
                val childName = activeChild?.name.orEmpty()
                val watchId = activeChild?.relojId

                if (watchId == null) {
                    clearLatestListener()
                    _sensorState.update {
                        it.copy(
                            childName = childName,
                            relojId = null,
                            heartRate = null,
                            movement = "Sin reloj",
                            status = "Vincula el reloj",
                            lastSync = "",
                            isLoading = false,
                            errorMessage = null,
                            registeredChildren = childrenList
                        )
                    }
                    return@launch
                }

                _sensorState.update {
                    it.copy(
                        childName = childName,
                        relojId = watchId,
                        isLoading = true,
                        errorMessage = null,
                        registeredChildren = childrenList
                    )
                }
                listenToLatestSensors(watchId)
            } catch (_: Exception) {
                clearLatestListener()
                _sensorState.update {
                    it.copy(
                        isLoading = false,
                        status = "No se pudo cargar",
                        errorMessage = "No pudimos conectar con Firebase."
                    )
                }
            }
        }
    }

    private suspend fun findAllChildrenForUser(uid: String): List<DocumentSnapshot> {
        val querySnapshot = firestore.collection("ninos")
            .whereEqualTo("padreId", uid)
            .get()
            .await()
        
        return querySnapshot.documents
    }

    private fun listenToLatestSensors(watchId: String) {
        if (currentWatchId == watchId && latestListener != null) return

        clearLatestListener()
        currentWatchId = watchId

        val reference = realtime.getReference("pending_wearables")
            .child(watchId)
            .child("latest")

        latestListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    _sensorState.update {
                        it.copy(
                            heartRate = null,
                            movement = "Sin datos",
                            status = "Esperando datos",
                            lastSync = "",
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    return
                }

                val heartRate = snapshot.child("hr").getValue(Int::class.java)
                    ?: snapshot.child("hr").getValue(Long::class.java)?.toInt()
                val accX = snapshot.child("acc").child("x").getValue(Double::class.java) ?: 0.0
                val accY = snapshot.child("acc").child("y").getValue(Double::class.java) ?: 0.0
                val accZ = snapshot.child("acc").child("z").getValue(Double::class.java) ?: 0.0
                val ts = snapshot.child("ts").getValue(Long::class.java)

                val movementValue = sqrt(accX * accX + accY * accY + accZ * accZ)
                val movement = when {
                    movementValue >= 15.0 -> "Rapido"
                    movementValue <= 7.0 -> "Lento"
                    else -> "Normal"
                }
                val status = when {
                    heartRate == null -> "Esperando datos"
                    heartRate >= 120 || movement == "Rapido" -> "Revisar"
                    else -> "Todo bien"
                }

                _sensorState.update {
                    it.copy(
                        heartRate = heartRate,
                        movement = movement,
                        status = status,
                        lastSync = ts?.let { value -> "Actualizado: ${formatTime(value)}" }.orEmpty(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _sensorState.update {
                    it.copy(
                        isLoading = false,
                        status = "No se pudo cargar",
                        errorMessage = "No pudimos leer los datos del reloj."
                    )
                }
            }
        }

        reference.addValueEventListener(latestListener!!)
    }

    private fun clearLatestListener() {
        val watchId = currentWatchId
        val listener = latestListener
        if (watchId != null && listener != null) {
            realtime.getReference("pending_wearables")
                .child(watchId)
                .child("latest")
                .removeEventListener(listener)
        }
        latestListener = null
        currentWatchId = null
    }

    fun onWatchCodeChange(code: String) {
        _sensorState.update { it.copy(watchCodeInput = code, errorMessage = null) }
    }

    fun connectWatch() {
        val uid = auth.currentUser?.uid ?: return
        val code = _sensorState.value.watchCodeInput.trim()

        if (code.isBlank()) {
            _sensorState.update { it.copy(errorMessage = "Por favor, ingresa el código del reloj.") }
            return
        }

        viewModelScope.launch {
            _sensorState.update { it.copy(isConnecting = true, errorMessage = null) }
            try {
                val query = firestore.collection("ninos")
                    .whereEqualTo("relojId", code)
                    .limit(1)
                    .get()
                    .await()

                val childDoc = query.documents.firstOrNull()

                if (childDoc != null) {
                    firestore.collection("ninos").document(childDoc.id)
                        .update("padreId", uid)
                        .await()

                    _sensorState.update { it.copy(isConnecting = false, watchCodeInput = "") }
                    loadChildAndWatch()
                } else {
                    _sensorState.update {
                        it.copy(
                            isConnecting = false,
                            errorMessage = "No encontramos un reloj con ese código."
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Esto imprimirá el error real en el Logcat
                _sensorState.update {
                    it.copy(
                        isConnecting = false,
                        errorMessage = "Error al conectar: ${e.localizedMessage}. Inténtalo de nuevo."
                    )
                }
            }
        }
    }

    private fun formatTime(value: Long): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(value))
    }

    override fun onCleared() {
        clearLatestListener()
        super.onCleared()
    }
}

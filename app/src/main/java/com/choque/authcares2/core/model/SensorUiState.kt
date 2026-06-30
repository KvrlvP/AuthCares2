package com.choque.authcares2.core.model

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
    val watchConnected: Boolean = false,
    val connectedWatchId: String? = null,
    val registeredChildren: List<ChildInfo> = emptyList(),
    val selectedChild: ChildInfo? = null
)

package com.choque.authcares2.features.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choque.authcares2.features.alerts.data.AlertsRepository
import com.choque.authcares2.features.alerts.data.FirebaseAlertsRepository
import com.choque.authcares2.features.alerts.domain.AlertDetector
import com.choque.authcares2.features.alerts.model.AlertItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlertsUiState(
    val alerts: List<AlertItem> = emptyList(),
    val selectedAlert: AlertItem? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class AlertsViewModel(
    private val repository: AlertsRepository = FirebaseAlertsRepository(),
    private val detector: AlertDetector = AlertDetector()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    private var historyJob: Job? = null
    private var observedWatchId: String? = null

    fun observeWatch(watchId: String?, childName: String) {
        if (watchId.isNullOrBlank()) {
            historyJob?.cancel()
            observedWatchId = null
            _uiState.value = AlertsUiState(
                isLoading = false,
                errorMessage = "No hay un reloj conectado."
            )
            return
        }
        if (observedWatchId == watchId && historyJob?.isActive == true) return

        observedWatchId = watchId
        historyJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        historyJob = viewModelScope.launch {
            repository.observeHistory(watchId).collect { result ->
                result.onSuccess { samples ->
                    val alerts = detector.detect(
                        samples = samples,
                        childName = childName.ifBlank { "El niño" }
                    )
                    _uiState.update { current ->
                        current.copy(
                            alerts = alerts,
                            selectedAlert = current.selectedAlert?.let { selected ->
                                alerts.firstOrNull { it.id == selected.id }
                            },
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }.onFailure {
                    _uiState.update { current ->
                        current.copy(
                            alerts = emptyList(),
                            isLoading = false,
                            errorMessage = "No pudimos leer el historial del reloj."
                        )
                    }
                }
            }
        }
    }

    fun selectAlert(alert: AlertItem) {
        _uiState.update { it.copy(selectedAlert = alert) }
    }
}

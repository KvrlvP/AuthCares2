package com.choque.authcares2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.choque.authcares2.BuildConfig
data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String = "",
    val hasDataCard: Boolean = false
)

class AsistenteViewModel(application: Application) : AndroidViewModel(application) {
    private val apiKey = BuildConfig.GOOGLE_AI_API_KEY

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        systemInstruction = content {
            text("""
                Eres el Asistente AuthCares. Tu propósito es ayudar a los padres y cuidadores a interpretar los datos de los relojes inteligentes de los niños a su cuidado, especialmente niños con tendencia a la ansiedad o neurodivergencia.
                Sé empático, claro y ofrece recomendaciones prácticas basadas en los datos que se te proporcionen.
                Si los datos indican estrés, adviértelo amablemente y sugiere estrategias de calma.
            """.trimIndent())
        }
    )

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        addMessage("¡Hola! Estoy analizando los datos en tiempo real. ¿En qué puedo ayudarte hoy?", isFromUser = false)
    }

    private fun getChildContext(sensorState: SensorUiState?): String {
        if (sensorState == null) return "No hay datos de sensores disponibles en este momento."

        val childName = sensorState.childName.ifBlank { "el niño" }
        val ritmoCardiaco = sensorState.heartRate
        val movimiento = sensorState.movement
        val status = sensorState.status
        val lastSync = sensorState.lastSync

        return """
            Contexto actual del niño ($childName):
            - Ritmo cardíaco: ${ritmoCardiaco ?: "No disponible"} lpm
            - Movimiento: $movimiento
            - Estado del sistema: $status
            - Última sincronización: $lastSync
            (Nota: Si el ritmo es > 100 y el movimiento es 'Lento', podría haber ansiedad)
        """.trimIndent()
    }

    fun sendMessage(userText: String, sensorState: SensorUiState? = null) {
        if (userText.isBlank()) return

        addMessage(userText, isFromUser = true)
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val contextPrompt = "${getChildContext(sensorState)}\n\nPregunta del padre: $userText"

                val response = generativeModel.generateContent(contextPrompt)
                val aiResponseText = response.text ?: "Lo siento, no pude procesar la información en este momento."

                val showDataCard = aiResponseText.contains("ritmo cardíaco", ignoreCase = true) || 
                                 aiResponseText.contains("bpm", ignoreCase = true)

                addMessage(aiResponseText, isFromUser = false, hasDataCard = showDataCard)

            } catch (e: Exception) {
                e.printStackTrace()
                addMessage("Error de conexión con la IA. Por favor intenta de nuevo.", isFromUser = false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun addMessage(text: String, isFromUser: Boolean, hasDataCard: Boolean = false) {
        _messages.value = _messages.value + ChatMessage(
            text = text,
            isFromUser = isFromUser,
            hasDataCard = hasDataCard
        )
    }
}
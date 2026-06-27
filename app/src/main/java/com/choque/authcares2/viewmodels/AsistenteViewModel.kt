package com.choque.authcares2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.choque.authcares2.core.model.SensorUiState
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String = "",
    val hasDataCard: Boolean = false
)

class AsistenteViewModel(application: Application) : AndroidViewModel(application) {

    private val generativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = "gemini-3.1-flash-lite",
            generationConfig = generationConfig {
                candidateCount = 1
                maxOutputTokens = 250
                temperature = 0.3f
            },
            systemInstruction = content {
                text(
                    """
                    Eres el Asistente AuthCares. Ayudas a padres y cuidadores a entender
                    información de un smartwatch de un niño con autismo o ansiedad.

                    Responde en español, con empatía y palabras sencillas.
                    Contesta únicamente lo que la persona preguntó.
                    Da primero una respuesta directa y, solo si ayuda, uno o dos pasos prácticos.
                    No enumeres todos los datos disponibles ni repitas el contexto recibido.
                    Usa solamente los datos relacionados con la pregunta.
                    Mantén la respuesta breve: máximo 100 palabras.
                    No diagnostiques ni reemplaces a un profesional de salud.
                    Si hay señales que podrían ser urgentes, recomienda buscar ayuda médica.
                    Si falta información esencial, haz una sola pregunta breve.
                    """.trimIndent()
                )
            }
        )
    }

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        addMessage(
            "¡Hola! Puedo ayudarte a entender los datos del reloj. ¿Qué quieres saber?",
            isFromUser = false
        )
    }

    private fun getRelevantChildContext(userText: String, sensorState: SensorUiState?): String {
        if (sensorState == null) return "No hay datos del reloj disponibles."

        val question = userText.lowercase()
        val asksHeartRate = listOf(
            "ritmo", "corazón", "corazon", "pulso", "bpm", "latido"
        ).any(question::contains)
        val asksMovement = listOf(
            "movimiento", "actividad", "moviendo", "quieto", "quieta", "camina"
        ).any(question::contains)
        val asksGeneralState = listOf(
            "cómo está", "como esta", "estado", "datos", "reloj", "sensor", "todo"
        ).any(question::contains)

        val relevantData = buildList {
            if (asksHeartRate || asksGeneralState) {
                add("Ritmo cardíaco: ${sensorState.heartRate?.let { "$it bpm" } ?: "sin datos"}")
            }
            if (asksMovement || asksGeneralState) {
                add("Movimiento: ${sensorState.movement}")
            }
            if (asksGeneralState) {
                add("Estado del reloj: ${sensorState.status}")
            }
        }

        if (relevantData.isEmpty()) {
            return "No se necesitan datos del reloj para responder esta pregunta."
        }

        val childName = sensorState.childName.ifBlank { "el niño" }
        return "Datos relevantes de $childName:\n${relevantData.joinToString("\n")}"
    }

    fun sendMessage(userText: String, sensorState: SensorUiState? = null) {
        if (userText.isBlank()) return

        addMessage(userText, isFromUser = true)
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val context = getRelevantChildContext(userText, sensorState)

                val response = generativeModel.generateContent(
                    content {
                        text(context)
                        text("Pregunta del cuidador: $userText")
                    }
                )

                val aiResponseText = response.text?.trim().takeUnless { it.isNullOrBlank() }
                    ?: "No pude preparar una respuesta. Intenta preguntar de otra forma."

                val showDataCard = aiResponseText.contains("ritmo cardíaco", ignoreCase = true) ||
                        aiResponseText.contains("bpm", ignoreCase = true)

                addMessage(aiResponseText, isFromUser = false, hasDataCard = showDataCard)
            } catch (e: Exception) {
                e.printStackTrace()
                addMessage(
                    "No pude conectarme con el asistente en este momento. Revisa tu conexión e inténtalo de nuevo.",
                    isFromUser = false
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun addMessage(text: String, isFromUser: Boolean, hasDataCard: Boolean = false) {
        _messages.value += ChatMessage(
            text = text,
            isFromUser = isFromUser,
            hasDataCard = hasDataCard
        )
    }
}

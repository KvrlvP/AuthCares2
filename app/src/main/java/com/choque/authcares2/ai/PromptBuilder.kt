package com.choque.authcares2.ai

object PromptBuilder {
    fun build(intent: IntentType, userQuestion: String, childContext: String): String {
        val basePrompt = """
            $childContext
            
            Pregunta del usuario: $userQuestion
            Intención detectada: ${intent.name}
            
            Instrucciones adicionales:
        """.trimIndent()

        return when (intent) {
            IntentType.CALM -> "$basePrompt Enfócate en sugerir técnicas de relajación, respiración o actividades sensoriales tranquilas."
            IntentType.HEART_RATE -> "$basePrompt Explica qué puede significar este ritmo cardíaco y si es necesario tomar alguna acción inmediata basada en el contexto."
            IntentType.SLEEP -> "$basePrompt Analiza la calidad del sueño y ofrece consejos para mejorar la higiene del sueño."
            IntentType.LOCATION -> "$basePrompt Informa sobre la ubicación actual de forma clara."
            IntentType.EMERGENCY -> "$basePrompt TRATA ESTO COMO UNA PRIORIDAD ALTA. Da instrucciones claras de seguridad."
            IntentType.GENERAL -> "$basePrompt Responde de forma empática y útil a la consulta general."
        }
    }
}

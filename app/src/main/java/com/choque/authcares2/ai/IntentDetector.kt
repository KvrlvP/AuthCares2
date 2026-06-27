package com.choque.authcares2.ai

object IntentDetector {

    fun detect(text: String): IntentType {

        val t = text.lowercase()

        return when {

            "calma" in t ||
                    "ansiedad" in t ||
                    "respirar" in t ||
                    "tranquil" in t ->
                IntentType.CALM

            "ritmo" in t ||
                    "corazón" in t ||
                    "pulso" in t ||
                    "lpm" in t ->
                IntentType.HEART_RATE

            "sueño" in t ||
                    "durmió" in t ->
                IntentType.SLEEP

            "ubicación" in t ||
                    "gps" in t ||
                    "dónde" in t ->
                IntentType.LOCATION

            "emergencia" in t ||
                    "desmayo" in t ->
                IntentType.EMERGENCY

            else ->
                IntentType.GENERAL
        }

    }

}
package com.choque.authcares2.features.alerts.model

import androidx.compose.ui.graphics.Color

data class AlertItem(
    val childName: String,
    val alertType: String,
    val description: String,
    val time: String,
    val iconRes: Int,
    val iconTint: Color,
    val iconBg: Color,
    val priorityColor: Color,
    val borderColor: Color
)
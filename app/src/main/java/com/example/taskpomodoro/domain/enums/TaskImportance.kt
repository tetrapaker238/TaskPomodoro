package com.example.taskpomodoro.domain.enums

import androidx.compose.ui.graphics.Color

enum class TaskImportance {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH;

    val color: Color
        get() = when (this) {
            LOW -> Color.LightGray
            MEDIUM -> Color.Blue
            HIGH -> Color(0xFFFFA500)
            VERY_HIGH -> Color.Yellow
        }
}
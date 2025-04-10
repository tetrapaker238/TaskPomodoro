package com.example.taskpomodoro.ui
import com.example.taskpomodoro.model.Timer

data class PomodoroUiState(
    val timeText: String,
    val buttonText: String,
    val timer: Timer,
    val counting: Boolean,
    val isOnBreak: Boolean,
)

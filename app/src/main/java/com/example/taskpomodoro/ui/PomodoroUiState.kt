package com.example.taskpomodoro.ui

import com.example.taskpomodoro.utils.StoppableCountDownTimer

data class PomodoroUiState(
    val timeText: String,
    val buttonText: String,
    val timer: StoppableCountDownTimer,
    val counting: Boolean,
)

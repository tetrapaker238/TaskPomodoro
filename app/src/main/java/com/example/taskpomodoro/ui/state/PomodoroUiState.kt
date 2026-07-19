package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.domain.dataclasses.PomodoroSettings

data class PomodoroUiState(
    val timeText: String,
    val buttonText: String,
    val counting: Boolean = false,
    val showDialog: Boolean = false,
    val finishedPomodoros: Int = 0,
    val pomodoroSettings: PomodoroSettings = PomodoroSettings(20, 5, 10, 4)
)
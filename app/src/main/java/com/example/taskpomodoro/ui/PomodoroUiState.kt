package com.example.taskpomodoro.ui
import com.example.taskpomodoro.model.Timer

data class PomodoroUiState(
    val timeText: String,
    val buttonText: String,
    val timer: Timer,
    val counting: Boolean,
    val pomodoroSettings: PomodoroSettings
)

data class PomodoroSettings(
    val pomodoroTime: Int,
    val breakTime: Int,
    val longBreakTime: Int,
    val longBreakInterval: Int,
)
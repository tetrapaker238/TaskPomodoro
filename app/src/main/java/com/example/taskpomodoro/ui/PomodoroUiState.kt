package com.example.taskpomodoro.ui

data class PomodoroUiState(
    val timeText: String,
    val buttonText: String,
    val counting: Boolean = false,
    val showDialog: Boolean = false,
    val finishedPomodoros: Int = 0,
    val pomodoroSettings: PomodoroSettings = PomodoroSettings(20, 5, 10, 4)
)

data class PomodoroSettings(
    val pomodoroTime: Int = 25,
    val breakTime: Int = 5,
    val longBreakTime: Int = 10,
    val longBreakInterval: Int = 4,
)
package com.example.taskpomodoro.domain.dataclasses

data class PomodoroSettings(
    val pomodoroTime: Int = 25,
    val breakTime: Int = 5,
    val longBreakTime: Int = 10,
    val longBreakInterval: Int = 4,
)
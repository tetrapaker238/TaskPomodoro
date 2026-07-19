package com.example.taskpomodoro.domain.dataclasses

import com.example.taskpomodoro.domain.enums.TaskImportance
import com.example.taskpomodoro.domain.enums.TaskStatus

data class PomodoroTask (
    val text: String = "",
    val importance: TaskImportance = TaskImportance.LOW,
    val status: TaskStatus = TaskStatus.UNDONE
)
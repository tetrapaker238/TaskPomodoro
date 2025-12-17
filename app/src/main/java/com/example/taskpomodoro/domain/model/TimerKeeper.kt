package com.example.taskpomodoro.domain.model

import com.example.taskpomodoro.data.dataclasses.PomodoroUiState
import com.example.taskpomodoro.ui.state.PomodoroState
import kotlinx.coroutines.flow.StateFlow

interface TimerKeeper {
    val timer: Timer
    val uiState: StateFlow<PomodoroUiState>

    fun setPomodoroState(newPomodoroState: PomodoroState)
}
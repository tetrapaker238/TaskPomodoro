package com.example.taskpomodoro.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes: NavKey {
    @Serializable
    data class PomodoroKey(val taskId: Int?): Routes()

    @Serializable
    data object Error: Routes()


}
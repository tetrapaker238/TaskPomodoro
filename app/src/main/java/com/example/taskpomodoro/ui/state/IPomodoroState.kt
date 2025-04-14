package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.ui.PomodoroViewModel

interface IPomodoroState {
    fun getButtonText(): String
    fun goToBreak()
    fun isCounting(): Boolean
    fun isOnBreak(): Boolean
    var pomodoroViewModel: PomodoroViewModel?
    fun start()
    fun stop()
}
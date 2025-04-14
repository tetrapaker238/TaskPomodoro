package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.ui.PomodoroViewModel

interface IPomodoroState {
    var pomodoroViewModel: PomodoroViewModel?
    fun getButtonText(): String
    fun stop()
    fun start()
    fun isOnBreak(): Boolean
    fun goToBreak()
}
package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.ui.PomodoroViewModel

abstract class PomodoroState : IPomodoroState {

    override var pomodoroViewModel: PomodoroViewModel? = null

    abstract override fun finish()

    abstract override fun getButtonText(): String

    abstract override fun isOnBreak(): Boolean

    abstract override fun stop()

    abstract override fun start()

    abstract override fun isCounting(): Boolean

}
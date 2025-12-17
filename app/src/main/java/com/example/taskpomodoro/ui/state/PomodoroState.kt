package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.presentation.viewmodel.PomodoroViewModel

abstract class PomodoroState(val stateType: EPomodoroButtonText) :
    IPomodoroState {

    override var pomodoroViewModel: PomodoroViewModel? = null

    override fun getButtonText(): String {
        val countPart: String =
            if (this.pomodoroViewModel?.timer?.isCounting() == true) "Stop" else "Start"
        return "$countPart ${stateType.buttonText}"
    }

    abstract override fun getInitialTime(): Int

    abstract override fun isOnBreak(): Boolean

    abstract override fun goNextTimerState()

}
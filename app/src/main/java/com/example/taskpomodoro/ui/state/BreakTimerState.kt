package com.example.taskpomodoro.ui.state

class BreakTimerState : PomodoroState(EPomodoroButtonText.BREAK) {

    override fun getInitialTime(): Int {
        return this.pomodoroViewModel?.uiState?.value?.pomodoroSettings?.breakTime ?: -1
    }

    override fun isOnBreak(): Boolean {
        return true
    }

    override fun goNextTimerState() {
        this.pomodoroViewModel?.setPomodoroState(PomodoroTimerState())
    }
}
package com.example.taskpomodoro.ui.state

class LongBreakTimerState : PomodoroState(EPomodoroButtonText.LONG_BREAK) {

    override fun getInitialTime(): Int {
        return this.pomodoroViewModel?.uiState?.value?.pomodoroSettings?.longBreakTime ?: -1
    }

    override fun isOnBreak(): Boolean {
        return true
    }

    override fun goNextTimerState() {
        this.pomodoroViewModel?.setPomodoroState(PomodoroTimerState())
    }
}
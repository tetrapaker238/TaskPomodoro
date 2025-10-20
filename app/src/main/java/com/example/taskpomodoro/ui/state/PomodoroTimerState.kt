package com.example.taskpomodoro.ui.state

class PomodoroTimerState : PomodoroState(EPomodoroButtonText.POMODORO) {

    override fun getInitialTime(): Int {
        return this.pomodoroViewModel?.uiState?.value?.pomodoroSettings?.pomodoroTime ?: -1
    }

    override fun isOnBreak(): Boolean {
        return false
    }

    override fun goNextTimerState() {
        if (this.pomodoroViewModel?.uiState?.value?.finishedPomodoros
            == this.pomodoroViewModel?.uiState?.value?.pomodoroSettings?.longBreakInterval
        ) {
            this.pomodoroViewModel?.setPomodoroState(LongBreakTimerState())
        } else {
            this.pomodoroViewModel?.setPomodoroState(BreakTimerState())
        }
    }

}
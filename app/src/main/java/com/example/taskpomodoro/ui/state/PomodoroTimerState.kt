package com.example.taskpomodoro.ui.state

class PomodoroTimerState : PomodoroState(EPomodoroButtonText.POMODORO) {

    override fun getInitialTime(): Int {
        return this.timerKeeper?.uiState?.value?.pomodoroSettings?.pomodoroTime ?: -1
    }

    override fun isOnBreak(): Boolean {
        return false
    }

    override fun goNextTimerState() {
        if (this.timerKeeper?.uiState?.value?.finishedPomodoros
            == this.timerKeeper?.uiState?.value?.pomodoroSettings?.longBreakInterval
        ) {
            this.timerKeeper?.setPomodoroState(LongBreakTimerState())
        } else {
            this.timerKeeper?.setPomodoroState(BreakTimerState())
        }
    }

}
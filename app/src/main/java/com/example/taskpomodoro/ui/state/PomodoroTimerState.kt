package com.example.taskpomodoro.ui.state

class PomodoroTimerState : PomodoroState(EPomodoroButtonText.POMODORO) {

    override fun getInitialTime(): Int {
        return this.pomodoroViewModel?.uiState?.value?.pomodoroSettings?.pomodoroTime ?: -1
    }

    override fun isOnBreak(): Boolean {
        return false
    }

    override fun goNextTimerState() {
        //TODO: Change to long break timer when long break interval condition has met given the
        // amount of pomodoros done inside the uiState value
        this.pomodoroViewModel?.setPomodoroState(BreakTimerState())
    }

}
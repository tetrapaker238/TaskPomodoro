package com.example.taskpomodoro.ui.state

class StartedBreak: PomodoroState() {
    override fun getButtonText(): String {
        return EButtonText.STOP_BREAK.buttonText
    }

    override fun stop() {
        this.pomodoroViewModel?.setPomodoroState(StoppedBreak())
    }

    override fun start() {}

    override fun goToBreak() {
        this.pomodoroViewModel?.setPomodoroState(CreatedPomodoro())
    }

    override fun isOnBreak(): Boolean {
        return true
    }
}
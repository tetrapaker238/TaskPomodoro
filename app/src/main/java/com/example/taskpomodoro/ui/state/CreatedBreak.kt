package com.example.taskpomodoro.ui.state

class CreatedBreak: PomodoroState() {
    override fun getButtonText(): String {
        return EButtonText.START_BREAK.buttonText
    }

    override fun stop() {}

    override fun start() {
        this.pomodoroViewModel?.setPomodoroState(StartedBreak())
    }

    override fun goToBreak() {}

    override fun isOnBreak(): Boolean {
        return true
    }
}
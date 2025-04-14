package com.example.taskpomodoro.ui.state

class StoppedBreak: PomodoroState() {

    override fun finish() {}

    override fun getButtonText(): String {
        return EButtonText.START_BREAK.buttonText
    }

    override fun isCounting(): Boolean {
        return false
    }

    override fun isOnBreak(): Boolean {
        return true
    }

    override fun start() {
        this.pomodoroViewModel?.setPomodoroState(StartedBreak())
    }

    override fun stop() {}

}
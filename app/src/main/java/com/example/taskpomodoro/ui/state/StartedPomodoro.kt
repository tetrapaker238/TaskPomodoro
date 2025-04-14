package com.example.taskpomodoro.ui.state

class StartedPomodoro: PomodoroState() {
    override fun getButtonText(): String {
        return EButtonText.STOP.buttonText
    }

    override fun stop() {
        this.pomodoroViewModel?.setPomodoroState(StoppedPomodoro())
    }

    override fun start() {}

    override fun goToBreak() {
        this.pomodoroViewModel?.setPomodoroState(CreatedBreak())
    }

    override fun isOnBreak(): Boolean {
        return false
    }

}
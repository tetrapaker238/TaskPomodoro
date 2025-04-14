package com.example.taskpomodoro.ui.state

class CreatedPomodoro : PomodoroState() {

    override fun getButtonText(): String {
        return EButtonText.START.buttonText
    }

    override fun stop() {}

    override fun start() {
        this.pomodoroViewModel?.setPomodoroState(StartedPomodoro())
    }

    override fun goToBreak() {}

    override fun isOnBreak(): Boolean {
        return false
    }
}
package com.example.taskpomodoro.ui.state

class CreatedPomodoro : PomodoroState() {

    override fun getButtonText(): String {
        return EButtonText.START.buttonText
    }

    override fun finish() {}

    override fun isCounting(): Boolean {
        return false
    }

    override fun isOnBreak(): Boolean {
        return false
    }

    override fun stop() {}

    override fun start() {
        this.pomodoroViewModel?.setPomodoroState(StartedPomodoro())
    }


}
package com.example.taskpomodoro.ui.state

class StartedPomodoro: PomodoroState() {

    override fun getButtonText(): String {
        return EButtonText.STOP.buttonText
    }

    override fun goToBreak() {
        this.pomodoroViewModel?.setPomodoroState(CreatedBreak())
    }

    override fun isCounting(): Boolean {
        return true
    }

    override fun isOnBreak(): Boolean {
        return false
    }

    override fun start() {}

    override fun stop() {
        this.pomodoroViewModel?.setPomodoroState(StoppedPomodoro())
    }



}
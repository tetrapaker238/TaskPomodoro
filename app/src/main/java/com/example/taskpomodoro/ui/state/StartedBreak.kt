package com.example.taskpomodoro.ui.state

class StartedBreak: PomodoroState() {

    override fun getButtonText(): String {
        return EButtonText.STOP_BREAK.buttonText
    }

    override fun goToBreak() {
        this.pomodoroViewModel?.setPomodoroState(CreatedPomodoro())
    }

    override fun isCounting(): Boolean {
        return true
    }

    override fun isOnBreak(): Boolean {
        return true
    }

    override fun start() {}

    override fun stop() {
        this.pomodoroViewModel?.setPomodoroState(StoppedBreak())
    }


}
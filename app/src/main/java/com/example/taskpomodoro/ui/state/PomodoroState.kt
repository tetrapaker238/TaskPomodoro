package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.domain.model.TimerKeeper


abstract class PomodoroState(val stateType: EPomodoroButtonText) :
    IPomodoroState {

    override var timerKeeper: TimerKeeper? = null

    override fun getButtonText(): String {
        val countPart: String =
            if (this.timerKeeper?.timer?.isCounting() == true) "Stop" else "Start"
        return "$countPart ${stateType.buttonText}"
    }

    override fun onTimerButtonClicked() {
        if (timerKeeper?.timer?.isCounting() == true) {
            timerKeeper?.timer?.stopTimer()
        } else {
            timerKeeper?.timer?.playTimer()
        }
    }

    abstract override fun getInitialTime(): Int

    abstract override fun isOnBreak(): Boolean

    abstract override fun goNextTimerState()

}
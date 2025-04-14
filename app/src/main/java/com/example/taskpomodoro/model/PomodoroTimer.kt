package com.example.taskpomodoro.model

import com.example.taskpomodoro.ui.PomodoroViewModel
import com.example.taskpomodoro.utils.StoppableCountDownTimer

class PomodoroTimer (
    val timeInMillis: Long
): Timer {
    private var totalTimeInMillis = timeInMillis
    private var intervalTime = 1000L

    private var stoppableCountDownTimer: StoppableCountDownTimer? = null

    override fun playTimer() {
        stoppableCountDownTimer?.playTimer()
    }

    override fun stopTimer() {
        stoppableCountDownTimer?.stopTimer()
    }

    override fun attach(pomodoroViewModel: PomodoroViewModel): PomodoroTimer {
        stoppableCountDownTimer = object: StoppableCountDownTimer(totalTimeInMillis, intervalTime) {
            override fun onTimerTick(millisUntilFinished: Long) {
                pomodoroViewModel.updateTimeText(millisUntilFinished)
            }

            override fun onTimerFinish() {
                pomodoroViewModel.updateStateOnFinish()
            }
        }
        return this
    }

    override fun setTotalTimeInMs(newTimeInMs: Long): Timer {
        this.totalTimeInMillis = newTimeInMs
        return this
    }

    override fun setIntervalTime(newIntervalTime: Long): Timer {
        this.intervalTime = newIntervalTime
        return this
    }

    override fun resetTimer(): Timer {
        stoppableCountDownTimer?.resetTimer(timeInMillis, intervalTime)
        return this
    }
}
package com.example.taskpomodoro.model

import com.example.taskpomodoro.ui.PomodoroViewModel
import com.example.taskpomodoro.utils.StoppableCountDownTimer

class PomodoroTimer (
    val timeInMillis: Long
): Timer {

    lateinit var stoppableCountDownTimer: StoppableCountDownTimer

    override fun playTimer() {
        stoppableCountDownTimer.playTimer()
    }

    override fun stopTimer() {
        stoppableCountDownTimer.stopTimer()
    }

    override fun attach(pomodoroViewModel: PomodoroViewModel) {
        stoppableCountDownTimer = object: StoppableCountDownTimer(timeInMillis, 1000) {
            override fun onTimerTick(millisUntilFinished: Long) {
                pomodoroViewModel.updateTimeText(millisUntilFinished)
            }

            override fun onTimerFinish() {
                pomodoroViewModel.updateStateOnFinish()
            }
        }
    }
}
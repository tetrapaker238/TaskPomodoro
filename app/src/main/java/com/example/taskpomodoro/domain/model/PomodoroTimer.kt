package com.example.taskpomodoro.domain.model

import com.example.taskpomodoro.utils.StoppableCountDownTimer

class PomodoroTimer(
    var timeInMillis: Long
) : Timer {
    private var intervalTime = 1000L

    private var stoppableCountDownTimer: StoppableCountDownTimer? = null

    override fun playTimer() {
        stoppableCountDownTimer?.playTimer()
    }

    override fun stopTimer() {
        stoppableCountDownTimer?.stopTimer()
    }

    override fun setListener(timerListener: TimerListener): PomodoroTimer {
        stoppableCountDownTimer =
            object : StoppableCountDownTimer(timeInMillis, intervalTime) {
                override fun onTimerTick(millisUntilFinished: Long) {
                    timerListener.onTimerTick(millisUntilFinished)
                }

                override fun onTimerFinish() {
                    timerListener.onTimerFinish()
                }
            }
        return this
    }

    override fun setTotalTimeInMs(newTimeInMs: Long): Timer {
        this.timeInMillis = newTimeInMs
        return this
    }

    override fun setIntervalTime(newIntervalTime: Long): Timer {
        this.intervalTime = newIntervalTime
        return this
    }

    override fun resetTimer(): Timer {
        stoppableCountDownTimer?.resetTimer(this.timeInMillis, intervalTime)
        return this
    }

    override fun isCounting(): Boolean {
        return this.stoppableCountDownTimer?.started == true
    }
}

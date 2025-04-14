package com.example.taskpomodoro.utils

import android.os.CountDownTimer

abstract class StoppableCountDownTimer(private val millisInFuture: Long, private val countDownInterval: Long) {
    private var _timer: CountDownTimer
    private var started: Boolean = false
    private var lastTimeInMs: Long = millisInFuture
    init {
        _timer = this.createTimer(millisInFuture, countDownInterval)
    }

    /**
     * Creates a new CountDownTimer given the time and interval in milliseconds
     * @param millisInFuture the total time in milliseconds of the timer
     * @param countDownInterval the interval time in milliseconds to trigger the onTick function
     * @return CountDownTimer
     */
    private fun createTimer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
        return object: CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                lastTimeInMs = millisUntilFinished
                onTimerTick(millisUntilFinished)
            }

            override fun onFinish() {
                lastTimeInMs = 0
                started = false
                onTimerFinish()
            }

        }
    }

    abstract fun onTimerTick(millisUntilFinished: Long)
    abstract fun onTimerFinish()

    /**
     * Starts the timer creating a new CountDownTimer only if it hasn't started or finished yet
     */
    fun playTimer() {
        if (started || lastTimeInMs.toInt() <= 0) {
            return
        }
        _timer = createTimer(lastTimeInMs, countDownInterval).start()
        started = true
    }

    /**
     * Stops the timer only if it has already started
     */
    fun stopTimer() {
        if (!started) {
            return
        }
        _timer.cancel()
        started = false
    }

    /**
     * Resets the timer creating a new one with possibly a different total time and or different
     * interval time
     * @param totalTime The total time of the new CountDownTimer
     * @param intervalTime The interval time of the new CountDownTimer
     */
    fun resetTimer(totalTime: Long = millisInFuture, intervalTime: Long = countDownInterval) {
        _timer.cancel()
        started = false
        _timer = createTimer(totalTime, intervalTime)
    }
}
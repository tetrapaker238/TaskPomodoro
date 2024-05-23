package com.example.taskpomodoro.utils

import android.os.CountDownTimer

abstract class StoppableCountDownTimer(private val millisInFuture: Long, private val countDownInterval: Long) {
    private var started: Boolean = false
    private lateinit var _timer: CountDownTimer
    private var lastTimeInMs: Long = millisInFuture
    private fun initTimer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
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

    fun playTimer() {
        if (!started) {
            if (lastTimeInMs.toInt() == 0) {
                lastTimeInMs = millisInFuture
            }
            _timer = initTimer(lastTimeInMs, countDownInterval).start()
            started = true
        }
    }

    fun stopTimer() {
        if (started) {
            _timer.cancel()
            started = false
        }
    }

}
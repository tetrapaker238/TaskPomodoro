package com.example.taskpomodoro.domain.model

interface TimerListener {
    fun onTimerTick(millisUntilFinished: Long)
    fun onTimerFinish()
}
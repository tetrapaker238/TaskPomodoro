package com.example.taskpomodoro.domain.model

interface Timer {
    fun playTimer()
    fun stopTimer()
    fun setListener(timerListener: TimerListener): Timer
    fun setTotalTimeInMs(newTimeInMs: Long): Timer
    fun setIntervalTime(newIntervalTime: Long): Timer
    fun resetTimer(): Timer
    fun isCounting(): Boolean
    fun destroyTimer()
}
package com.example.taskpomodoro.domain.model

import com.example.taskpomodoro.presentation.viewmodel.PomodoroViewModel

interface Timer {
    fun playTimer()
    fun stopTimer()
    fun attach(pomodoroViewModel: PomodoroViewModel): Timer
    fun setTotalTimeInMs(newTimeInMs: Long): Timer
    fun setIntervalTime(newIntervalTime: Long): Timer
    fun resetTimer(): Timer
    fun isCounting(): Boolean
}
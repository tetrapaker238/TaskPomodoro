package com.example.taskpomodoro.model
import com.example.taskpomodoro.ui.PomodoroViewModel

interface Timer {
    fun playTimer()
    fun stopTimer()
    fun attach(pomodoroViewModel: PomodoroViewModel)

}
package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.ui.PomodoroViewModel

interface IPomodoroState {

    /**
     * Returns the text that should be displayed on the timer's main button,
     * based on the current state.
     */
    fun getButtonText(): String

    /**
     * Handles logic for finishing the current session or break,
     * transitioning to the next appropriate state.
     */
    fun finish()

    /**
     * Indicates whether the timer is currently counting.
     * @return true if the timer is active, false otherwise.
     */
    fun isCounting(): Boolean

    /**
     * Indicates whether the current state is a break.
     * @return true if the timer is on a break, false otherwise.
     */
    fun isOnBreak(): Boolean

    /**
     * Reference to the [PomodoroViewModel] managing the timer state.
     * This allows the state to trigger changes in the ViewModel.
     */
    var pomodoroViewModel: PomodoroViewModel?

    /**
     * Triggers logic for entering a running state.
     */
    fun start()

    /**
     * Triggers logic for exiting a running state.
     */
    fun stop()
}
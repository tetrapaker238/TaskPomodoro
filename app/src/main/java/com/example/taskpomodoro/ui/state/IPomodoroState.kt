package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.ui.PomodoroViewModel

interface IPomodoroState {

    /**
     * Returns the text that should be displayed on the timer's main button,
     * based on the current state.
     */
    fun getButtonText(): String

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
     * Gets the initial time for the actual timer
     */
    fun getInitialTime(): Int

    /**
     * Sets the next timer state given the actual state
     */
    fun goNextTimerState()
}
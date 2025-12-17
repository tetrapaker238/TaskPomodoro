package com.example.taskpomodoro.ui.state

import com.example.taskpomodoro.domain.model.TimerKeeper

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


    var timerKeeper: TimerKeeper?

    /**
     * Gets the initial time for the actual timer
     */
    fun getInitialTime(): Int

    /**
     * Sets the next timer state given the actual state
     */
    fun goNextTimerState()

    /**
     * Handles the timer button click.
     */
    fun onTimerButtonClicked()
}
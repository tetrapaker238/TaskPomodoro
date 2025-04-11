package com.example.taskpomodoro.ui

import androidx.lifecycle.ViewModel
import com.example.taskpomodoro.model.PomodoroTimer
import com.example.taskpomodoro.model.Timer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PomodoroViewModel (
    timer: Timer? = null
): ViewModel() {

    private fun convertSecondsToMilliseconds(seconds: Long): Long {
        return 1000 * 60 * seconds
    }

    private val pomodoroTime: Long = convertSecondsToMilliseconds(25)
    private val pomodoroBreakTime: Long = convertSecondsToMilliseconds(5)
    private val initTimer: Timer = timer?: PomodoroTimer(pomodoroTime)

    init {
        initTimer.attach(this)
    }

    enum class ButtonText(val buttonText: String) {
        STOP("Stop pomodoro"),
        START("Start pomodoro"),
        START_BREAK("Start break"),
        STOP_BREAK("Stop break")
    }

    private fun getTimeFromMs(millis: Long): String {
        val minutes = (millis / (1000 * 60)).toInt()
        val seconds = ((millis - (minutes * 1000 * 60)) / 1000).toInt()
        val strMinutes = if (minutes >= 10) minutes.toString() else "0$minutes"
        val strSeconds = if (seconds >= 10) seconds.toString() else "0$seconds"
        return "$strMinutes:$strSeconds"
    }

    internal fun updateTimeText(millisUntilFinished: Long) {
        _uiState.update {
            it.copy(
                timeText = getTimeFromMs(millisUntilFinished),
            )
        }
    }

    internal fun getStartButtonText(isOnBreak: Boolean): String {
        return if (isOnBreak) ButtonText.START_BREAK.buttonText else ButtonText.START.buttonText
    }

    internal fun getStopButtonText(isOnBreak: Boolean): String {
        return if (isOnBreak) ButtonText.STOP_BREAK.buttonText else ButtonText.STOP.buttonText
    }

    private fun getTimeText(isOnBreak: Boolean): String {
        val newTime = if (isOnBreak) pomodoroBreakTime else pomodoroTime
        return getTimeFromMs(newTime)
    }

    internal fun updateStateOnFinish() {
        _uiState.update {
            val newIsOnBreak = !it.isOnBreak
            it.copy(
                isOnBreak = newIsOnBreak,
                counting = false,
                buttonText = getStartButtonText(newIsOnBreak),
                timeText = getTimeText(newIsOnBreak)
            )
        }
    }

    //TODO: Depending on which amount of time user selected, we have to pass values to the state
    private val _uiState = MutableStateFlow(PomodoroUiState(
        getTimeFromMs(pomodoroTime),
        ButtonText.START.buttonText,
        timer = initTimer,
        false,
        false,
    ))
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    fun startPomodoro() {
        _uiState.update {
            it.copy(
                counting = true,
                buttonText = getStopButtonText(it.isOnBreak),
            )
        }
        uiState.value.timer.playTimer()
    }

    fun stopPomodoro() {
        uiState.value.timer.stopTimer()
        _uiState.update {
            it.copy(
                counting = false,
                buttonText = getStartButtonText(it.isOnBreak)
            )
        }
    }

    //TODO: When pomodoro timer stops to 0, then recreate the timer with the next time (break time)
    // so it's like PomodoroTimer(pomodoroBreakTime) and then timer.attach(this)

    // TODO: Evaluate removing the functionality that resets the timer whenever it gets to zero and
    //  instead just force the viewmodel to create another pomodoro, so it's clear that another
    //  countdown it's being set
}
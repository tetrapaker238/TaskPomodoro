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
    private val dummyMillis: Long = 1000*25*60
    private val initTimer: Timer = timer?: PomodoroTimer(dummyMillis)

    init {
        initTimer.attach(this)
    }

    enum class ButtonText(val buttonText: String) {
        STOP("Stop pomodoro"),
        START("Start pomodoro")

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

    internal fun updateStateOnFinish() {
        _uiState.update {
            it.copy(
                counting = false,
                buttonText = ButtonText.START.buttonText,
                timeText = getTimeFromMs(dummyMillis)
            )
        }
    }

    //TODO: Depending on which amount of time user selected, we have to pass values to the state
    private val _uiState = MutableStateFlow(PomodoroUiState(
        getTimeFromMs(dummyMillis),
        ButtonText.START.buttonText,
        timer = initTimer,
        false,
    ))
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    fun startPomodoro() {
        _uiState.update {
            it.copy(
                counting = true,
                buttonText = ButtonText.STOP.buttonText,
            )
        }
        uiState.value.timer.playTimer()
    }

    fun stopPomodoro() {
        uiState.value.timer.stopTimer()
        _uiState.update {
            it.copy(
                counting = false,
                buttonText = ButtonText.START.buttonText
            )
        }
    }
}
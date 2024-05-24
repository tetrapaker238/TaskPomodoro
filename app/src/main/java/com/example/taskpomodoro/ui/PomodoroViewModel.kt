package com.example.taskpomodoro.ui

import androidx.lifecycle.ViewModel
import com.example.taskpomodoro.utils.StoppableCountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PomodoroViewModel: ViewModel() {
    val dummyMillis: Long = 1000*25*60

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

    private val initTimer: StoppableCountDownTimer = object: StoppableCountDownTimer(dummyMillis, 1000) {
        override fun onTimerTick(millisUntilFinished: Long) {

            _uiState.update {
                it.copy(
                    timeText = getTimeFromMs(millisUntilFinished),
                )
            }
        }

        override fun onTimerFinish() {
            _uiState.update {
                it.copy(
                    counting = false,
                    buttonText = ButtonText.START.buttonText,
                    timeText = getTimeFromMs(dummyMillis)
                )
            }
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
        uiState.value.timer.playTimer()
        _uiState.update {
            it.copy(
                counting = true,
                buttonText = ButtonText.STOP.buttonText,
            )
        }
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
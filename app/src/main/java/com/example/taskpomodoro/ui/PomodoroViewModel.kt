package com.example.taskpomodoro.ui

import androidx.lifecycle.ViewModel
import com.example.taskpomodoro.model.PomodoroTimer
import com.example.taskpomodoro.model.Timer
import com.example.taskpomodoro.ui.state.CreatedPomodoro
import com.example.taskpomodoro.ui.state.PomodoroState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PomodoroViewModel (
    timer: Timer? = null
): ViewModel() {
    private lateinit var pomodoroState: PomodoroState

    fun setPomodoroState(newPomodoroState: PomodoroState) {
        this.pomodoroState = newPomodoroState
        this.pomodoroState.pomodoroViewModel = this
    }
    init {
        setPomodoroState(CreatedPomodoro())
    }

    private fun convertSecondsToMilliseconds(seconds: Long): Long {
        return 1000 * 60 * seconds
    }

    private val pomodoroTime: Long = convertSecondsToMilliseconds(25)
    private val pomodoroBreakTime: Long = convertSecondsToMilliseconds(5)
    private var initTimer: Timer = timer?.attach(this) ?: PomodoroTimer(pomodoroTime).attach(this)

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

    private fun getAttachedPomodoroTime(timer: Timer): Timer {
        return timer.setTotalTimeInMs(if (pomodoroState.isOnBreak()) pomodoroBreakTime else pomodoroTime).resetTimer().attach(this)
    }

    private fun getTimeText(): String {
        val newTime = if (pomodoroState.isOnBreak()) pomodoroBreakTime else pomodoroTime
        return getTimeFromMs(newTime)
    }

    internal fun updateStateOnFinish() {
        pomodoroState.goToBreak()
        _uiState.update {
            it.copy(
                counting = false,
                buttonText = pomodoroState.getButtonText(),
                timeText = getTimeText(),
                timer = getAttachedPomodoroTime(it.timer)
            )
        }
    }

    //TODO: Depending on which amount of time user selected, we have to pass values to the state
    private val _uiState = MutableStateFlow(PomodoroUiState(
        getTimeFromMs(pomodoroTime),
        pomodoroState.getButtonText(),
        timer = initTimer,
        false,
    ))
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    fun startPomodoro() {
        pomodoroState.start()
        _uiState.update {
            it.copy(
                counting = true,
                buttonText = pomodoroState.getButtonText(),
            )
        }
        uiState.value.timer.playTimer()
    }

    fun stopPomodoro() {
        pomodoroState.stop()
        uiState.value.timer.stopTimer()
        _uiState.update {
            it.copy(
                counting = false,
                buttonText = pomodoroState.getButtonText()
            )
        }
    }
}
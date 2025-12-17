package com.example.taskpomodoro.ui

import androidx.lifecycle.ViewModel
import com.example.taskpomodoro.domain.model.PomodoroTimer
import com.example.taskpomodoro.domain.model.Timer
import com.example.taskpomodoro.ui.state.PomodoroState
import com.example.taskpomodoro.ui.state.PomodoroTimerState
import com.example.taskpomodoro.utils.convertMinutesToMilliseconds
import com.example.taskpomodoro.utils.getTimeFromMs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PomodoroViewModel(
    initialTimer: Timer? = null
) : ViewModel() {
    private lateinit var pomodoroState: PomodoroState
    internal val timer: Timer

    init {
        setPomodoroState(PomodoroTimerState())
    }

    fun setPomodoroState(newPomodoroState: PomodoroState) {
        this.pomodoroState = newPomodoroState
        this.pomodoroState.pomodoroViewModel = this
    }

    private val _uiState = MutableStateFlow(
        PomodoroUiState(
            getTimeFromMs(convertMinutesToMilliseconds(PomodoroSettings().pomodoroTime)),
            pomodoroState.getButtonText(),
            counting = false,
            showDialog = false,
            pomodoroSettings = PomodoroSettings()
        )
    )
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    init {
        this.timer = initialTimer?.attach(this) ?: PomodoroTimer(
            convertMinutesToMilliseconds(
                uiState.value.pomodoroSettings.pomodoroTime
            )
        ).attach(
            this
        )
    }

    internal fun updateTimeText(millisUntilFinished: Long) {
        _uiState.update {
            it.copy(
                timeText = getTimeFromMs(millisUntilFinished),
            )
        }
    }

    private fun resetTimer(timer: Timer?): Timer? {
        val timeInMs =
            convertMinutesToMilliseconds(if (pomodoroState.isOnBreak()) uiState.value.pomodoroSettings.breakTime else uiState.value.pomodoroSettings.pomodoroTime)
        return timer?.setTotalTimeInMs(timeInMs)?.resetTimer()
    }

    private fun getInitialTime(): Int {
        return pomodoroState.getInitialTime()
    }


    private fun getTimeText(): String {
        val newTime =
            convertMinutesToMilliseconds(this.getInitialTime())
        return getTimeFromMs(newTime)
    }

    private fun resetTimer(timer: Timer, time: Int) {
        timer.setTotalTimeInMs(convertMinutesToMilliseconds(time))
        timer.resetTimer()
    }

    internal fun updateStateOnFinish() {
        val isPomodoroFinished =
            uiState.value.finishedPomodoros == uiState.value.pomodoroSettings.longBreakInterval
        _uiState.update {
            it.copy(
                finishedPomodoros = if (isPomodoroFinished) 0 else it.finishedPomodoros + 1
            )
        }
        this.pomodoroState.goNextTimerState()
        resetTimer(timer)
        _uiState.update {
            it.copy(
                counting = false,
                buttonText = pomodoroState.getButtonText(),
                timeText = getTimeText(),
            )
        }
    }


    fun startPomodoro() {
        timer.playTimer()
        _uiState.update {
            it.copy(
                counting = true,
                buttonText = pomodoroState.getButtonText(),
            )
        }
    }

    fun stopPomodoro() {
        timer.stopTimer()
        _uiState.update {
            it.copy(
                counting = false,
                buttonText = pomodoroState.getButtonText()
            )
        }
    }

    fun toggleDialog() {
        _uiState.update {
            it.copy(
                showDialog = !it.showDialog
            )
        }
    }

    fun updateSettings(settings: PomodoroSettings) {
        _uiState.update {
            it.copy(pomodoroSettings = settings)
        }
        _uiState.update {
            resetTimer(timer, this.getInitialTime())
            it.copy(
                counting = false,
                buttonText = pomodoroState.getButtonText(),
                timeText = getTimeText(),
            )
        }
    }
}
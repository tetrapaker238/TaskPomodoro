package com.example.taskpomodoro.ui

import androidx.lifecycle.ViewModel
import com.example.taskpomodoro.model.PomodoroTimer
import com.example.taskpomodoro.model.Timer
import com.example.taskpomodoro.ui.state.CreatedPomodoro
import com.example.taskpomodoro.ui.state.PomodoroState
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
    private val timer: Timer

    init {
        setPomodoroState(CreatedPomodoro())
    }

    fun setPomodoroState(newPomodoroState: PomodoroState) {
        this.pomodoroState = newPomodoroState
        this.pomodoroState.pomodoroViewModel = this
    }

    private val _uiState = MutableStateFlow(
        PomodoroUiState(
            getTimeFromMs(convertMinutesToMilliseconds(PomodoroSettings().pomodoroTime)),
            pomodoroState.getButtonText(),
            pomodoroState.isCounting(),
            showDialog = false,
            PomodoroSettings()
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

    private fun getTime(): Int {
        return if (pomodoroState.isOnBreak()) uiState.value.pomodoroSettings.breakTime else uiState.value.pomodoroSettings.pomodoroTime
    }


    private fun getTimeText(): String {
        // TODO: Remove the .isOnBreak dependency, and get the settings break time or pomodoroTime
        //  when corresponding instead
        val newTime =
            convertMinutesToMilliseconds(this.getTime())
        return getTimeFromMs(newTime)
    }

    private fun resetTimer(timer: Timer, time: Int) {
        timer.setTotalTimeInMs(convertMinutesToMilliseconds(time))
        timer.resetTimer()
    }

    internal fun updateStateOnFinish() {
        pomodoroState.finish()
        resetTimer(timer)
        _uiState.update {
            it.copy(
                counting = pomodoroState.isCounting(),
                buttonText = pomodoroState.getButtonText(),
                timeText = getTimeText(),
            )
        }
    }


    fun startPomodoro() {
        pomodoroState.start()
        _uiState.update {
            it.copy(
                counting = pomodoroState.isCounting(),
                buttonText = pomodoroState.getButtonText(),
            )
        }
        timer.playTimer()
    }

    fun stopPomodoro() {
        pomodoroState.stop()
        timer.stopTimer()
        _uiState.update {
            it.copy(
                counting = pomodoroState.isCounting(), buttonText = pomodoroState.getButtonText()
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
        pomodoroState.stop()
        _uiState.update {
            it.copy(pomodoroSettings = settings)
        }
        _uiState.update {
            resetTimer(timer, this.getTime())
            it.copy(
                counting = pomodoroState.isCounting(),
                buttonText = pomodoroState.getButtonText(),
                timeText = getTimeText(),
            )
        }
    }
}
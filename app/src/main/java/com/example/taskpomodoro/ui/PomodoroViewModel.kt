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

class PomodoroViewModel(
    timer: Timer? = null
) : ViewModel() {
    private lateinit var pomodoroState: PomodoroState

    fun setPomodoroState(newPomodoroState: PomodoroState) {
        this.pomodoroState = newPomodoroState
        this.pomodoroState.pomodoroViewModel = this
    }

    init {
        setPomodoroState(CreatedPomodoro())
    }

    private fun convertMinutesToMilliseconds(minutes: Int): Long {
        return (1000 * 60 * minutes).toLong()
    }

    val pomodoroSettings = PomodoroSettings()
    private var initTimer: Timer = timer?.attach(this)
        ?: PomodoroTimer(convertMinutesToMilliseconds(pomodoroSettings.pomodoroTime)).attach(this)

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
        val timeInMs =
            convertMinutesToMilliseconds(if (pomodoroState.isOnBreak()) pomodoroSettings.breakTime else pomodoroSettings.pomodoroTime)
        return timer.setTotalTimeInMs(timeInMs).resetTimer().attach(this)
    }

    private fun getTimeText(): String {
        // TODO: Remove the .isOnBreak dependency, and get the settings break time or pomodoroTime
        //  when corresponding instead
        val newTime =
            convertMinutesToMilliseconds(if (pomodoroState.isOnBreak()) pomodoroSettings.breakTime else pomodoroSettings.pomodoroTime)
        return getTimeFromMs(newTime)
    }

    private fun resetTimer(timer: Timer, pomodoroTime: Int) {
        timer.setTotalTimeInMs(convertMinutesToMilliseconds(pomodoroTime))
        timer.resetTimer()
    }

    internal fun updateStateOnFinish() {
        pomodoroState.finish()
        _uiState.update {
            it.copy(
                counting = pomodoroState.isCounting(),
                buttonText = pomodoroState.getButtonText(),
                timeText = getTimeText(),
                timer = getAttachedPomodoroTime(it.timer)
            )
        }
    }

    private val _uiState = MutableStateFlow(
        PomodoroUiState(
            getTimeFromMs(convertMinutesToMilliseconds(pomodoroSettings.pomodoroTime)),
            pomodoroState.getButtonText(),
            timer = initTimer,
            pomodoroState.isCounting(),
            showDialog = false,
            pomodoroSettings
        )
    )
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    fun startPomodoro() {
        pomodoroState.start()
        _uiState.update {
            it.copy(
                counting = pomodoroState.isCounting(),
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
        _uiState.update {
            resetTimer(it.timer, settings.pomodoroTime)
            it.copy(
                timeText = getTimeFromMs(convertMinutesToMilliseconds(settings.pomodoroTime)),
                pomodoroSettings = settings
            )
        }
    }
}
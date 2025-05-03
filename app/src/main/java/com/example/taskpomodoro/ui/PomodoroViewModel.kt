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

    private fun convertMinutesToMilliseconds(minutes: Int): Long {
        return (1000 * 60 * minutes).toLong()
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

    private fun resetTimer(timer: Timer?): Timer? {
        val timeInMs =
            convertMinutesToMilliseconds(if (pomodoroState.isOnBreak()) uiState.value.pomodoroSettings.breakTime else uiState.value.pomodoroSettings.pomodoroTime)
        return timer?.setTotalTimeInMs(timeInMs)?.resetTimer()
    }

    private fun getTimeText(): String {
        // TODO: Remove the .isOnBreak dependency, and get the settings break time or pomodoroTime
        //  when corresponding instead
        val newTime =
            convertMinutesToMilliseconds(if (pomodoroState.isOnBreak()) uiState.value.pomodoroSettings.breakTime else uiState.value.pomodoroSettings.pomodoroTime)
        return getTimeFromMs(newTime)
    }

    private fun resetTimer(timer: Timer, pomodoroTime: Int) {
        timer.setTotalTimeInMs(convertMinutesToMilliseconds(pomodoroTime))
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
            resetTimer(timer, settings.pomodoroTime)
            it.copy(
                counting = pomodoroState.isCounting(),
                buttonText = pomodoroState.getButtonText(),
                timeText = getTimeText(),
                pomodoroSettings = settings
            )
        }
    }
}
package com.example.taskpomodoro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskpomodoro.data.dataclasses.PomodoroSettings
import com.example.taskpomodoro.data.dataclasses.PomodoroUiState
import com.example.taskpomodoro.domain.audio.SoundProvider
import com.example.taskpomodoro.domain.model.PomodoroTimer
import com.example.taskpomodoro.domain.model.Timer
import com.example.taskpomodoro.domain.model.TimerKeeper
import com.example.taskpomodoro.domain.model.TimerListener
import com.example.taskpomodoro.ui.state.PomodoroState
import com.example.taskpomodoro.ui.state.PomodoroTimerState
import com.example.taskpomodoro.utils.convertMinutesToMilliseconds
import com.example.taskpomodoro.utils.getTimeFromMs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.taskpomodoro.data.audio.SoundManager

class PomodoroViewModel(
    initialTimer: Timer? = null,
    val soundProvider: SoundProvider? = null
) : ViewModel(), TimerListener, TimerKeeper {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val soundManager = SoundManager(application.applicationContext)
                PomodoroViewModel(soundProvider = soundManager)
            }
        }
    }

    private lateinit var pomodoroState: PomodoroState
    override val timer: Timer

    init {
        setPomodoroState(PomodoroTimerState())
    }

    override fun setPomodoroState(newPomodoroState: PomodoroState) {
        this.pomodoroState = newPomodoroState
        this.pomodoroState.timerKeeper = this
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
    override val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    init {
        this.timer = initialTimer?.setListener(this) ?: PomodoroTimer(
            convertMinutesToMilliseconds(
                uiState.value.pomodoroSettings.pomodoroTime
            )
        ).setListener(
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
        this.soundProvider?.playInitSound()
        timer.playTimer()
        _uiState.update {
            it.copy(
                counting = true,
                buttonText = pomodoroState.getButtonText(),
            )
        }
    }

    fun stopPomodoro() {
        this.soundProvider?.playStopSound()
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

    override fun onTimerTick(millisUntilFinished: Long) {
        updateTimeText(millisUntilFinished)
    }

    override fun onTimerFinish() {
        soundProvider?.playFinishSound()
        updateStateOnFinish()
    }
}
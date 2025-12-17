package com.example.taskpomodoro

import com.example.taskpomodoro.data.dataclasses.PomodoroSettings
import com.example.taskpomodoro.domain.model.Timer
import com.example.taskpomodoro.presentation.viewmodel.PomodoroViewModel
import com.example.taskpomodoro.ui.state.EPomodoroButtonText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

/**
 * FakeTimer: Fake Timer to test PomodoroViewModel functionality. Allows going to next tick
 * and go to finish time
 * timeInMillis: Long -> Starting pomodoro time
 */
private class FakeTimer(timeInMillis: Long) : Timer {

    private var actualTimeInMillis = timeInMillis
    private var intervalTime = 1000L
    private lateinit var pomodoroViewModel: PomodoroViewModel
    private var counting = false
    override fun playTimer() {
        this.counting = true
    }

    override fun stopTimer() {
        this.counting = false
    }

    override fun attach(pomodoroViewModel: PomodoroViewModel): FakeTimer {
        this.pomodoroViewModel = pomodoroViewModel
        return this
    }

    override fun setTotalTimeInMs(newTimeInMs: Long): Timer {
        this.actualTimeInMillis = newTimeInMs
        return this
    }

    override fun setIntervalTime(newIntervalTime: Long): Timer {
        this.intervalTime = newIntervalTime
        return this
    }

    override fun resetTimer(): Timer {
        this.counting = false
        return this
    }

    override fun isCounting(): Boolean {
        return this.counting
    }

    fun nextTick() {
        actualTimeInMillis -= 1000
        pomodoroViewModel.updateTimeText(actualTimeInMillis)
    }

    fun goToFinish() {
        this.counting = false
        actualTimeInMillis = 0
        pomodoroViewModel.updateStateOnFinish()
    }
}

class PomodoroViewModelTest {
    private val dummyMillis: Long = 1000 * 25 * 60
    private val fakeTimer: FakeTimer = FakeTimer(dummyMillis)
    private val pomodoroViewModel = PomodoroViewModel(fakeTimer)
    private var state = pomodoroViewModel.uiState.value

    @Test
    fun initialUiState_Construct_NonStartingState() {
        assertEquals("25:00", state.timeText)
        assertEquals("Start ${EPomodoroButtonText.POMODORO.buttonText}", state.buttonText)
        assertFalse(state.counting)
    }

    @Test
    fun onStartTimerState_StartTimer_StartCountingAndChangeButtonText() {
        pomodoroViewModel.startPomodoro()
        state = pomodoroViewModel.uiState.value
        assertTrue(state.counting)
        assertEquals("Stop ${EPomodoroButtonText.POMODORO.buttonText}", state.buttonText)
    }

    @Test
    fun onFirstNextTickState_SecondPassed_UpdateTimeText() {
        pomodoroViewModel.startPomodoro()
        fakeTimer.nextTick()
        state = pomodoroViewModel.uiState.value
        assertEquals("24:59", state.timeText)
    }

    @Test
    fun onStopUiState_StopTimer_StopCounting() {
        pomodoroViewModel.startPomodoro()
        fakeTimer.nextTick()
        pomodoroViewModel.stopPomodoro()
        state = pomodoroViewModel.uiState.value
        assertFalse(state.counting)
    }

    @Test
    fun onFinishTickState_OnFinishTimer_UpdateBreakStateOnFinish() {
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        state = pomodoroViewModel.uiState.value
        assertFalse(state.counting)
        assertEquals("Start ${EPomodoroButtonText.BREAK.buttonText}", state.buttonText)
        assertEquals("05:00", state.timeText)
    }

    @Test
    fun startBreakState_OnStartBreak_StartCountingAndChangeBreakButtonText() {
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        pomodoroViewModel.startPomodoro()
        state = pomodoroViewModel.uiState.value
        assertTrue(state.counting)
        assertEquals("Stop ${EPomodoroButtonText.BREAK.buttonText}", state.buttonText)
    }

    @Test
    fun onFirstNextTickBreakState_SecondPassed_UpdateWithBreakTimeText() {
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        pomodoroViewModel.startPomodoro()
        fakeTimer.nextTick()
        state = pomodoroViewModel.uiState.value
        assertEquals("04:59", state.timeText)
    }

    @Test
    fun stopBreakUiState_OnStopTimer_StopCountingAndChangeBreakButtonText() {
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        pomodoroViewModel.startPomodoro()
        fakeTimer.nextTick()
        pomodoroViewModel.stopPomodoro()
        state = pomodoroViewModel.uiState.value
        assertFalse(state.counting)
        assertEquals("Start ${EPomodoroButtonText.BREAK.buttonText}", state.buttonText)
    }

    @Test
    fun settingsChange_OnSettingsChange_ChangeUiState() {
        val newPomodoroTime = 50
        val newBreakTime = 10
        val newLongBreakTime = 15
        val newLongBreakInterval = 6
        pomodoroViewModel.updateSettings(
            PomodoroSettings(
                pomodoroTime = newPomodoroTime,
                breakTime = newBreakTime,
                longBreakTime = newLongBreakTime,
                longBreakInterval = newLongBreakInterval
            )
        )

        state = pomodoroViewModel.uiState.value
        val pomodoroSettings = state.pomodoroSettings
        assertTrue(pomodoroSettings.pomodoroTime == newPomodoroTime)
        assertTrue(pomodoroSettings.breakTime == newBreakTime)
        assertTrue(pomodoroSettings.longBreakTime == newLongBreakTime)
        assertTrue(pomodoroSettings.longBreakInterval == newLongBreakInterval)
        assertFalse(state.counting)
        assertEquals(state.timeText, "50:00")
        assertEquals("Start ${EPomodoroButtonText.POMODORO.buttonText}", state.buttonText)
        assertFalse(state.showDialog)
    }

    @Test
    fun onFirstNextTickSettingsChangedState_OnTickPassed_TimeTextChanged() {
        val newPomodoroTime = 50
        val newBreakTime = 10
        val newLongBreakTime = 15
        val newLongBreakInterval = 6
        pomodoroViewModel.updateSettings(
            PomodoroSettings(
                pomodoroTime = newPomodoroTime,
                breakTime = newBreakTime,
                longBreakTime = newLongBreakTime,
                longBreakInterval = newLongBreakInterval
            )
        )
        pomodoroViewModel.startPomodoro()
        fakeTimer.nextTick()
        state = pomodoroViewModel.uiState.value
        assertEquals(state.timeText, "49:59")
    }

    @Test
    fun onUpdateSettingsWhilePomodoroIsCounting_OnUpdatePomodoroSettings_ButtonTextIsStartAndTimeTextIsPomodoro() {
        val newPomodoroTime = 50
        val newBreakTime = 10
        val newLongBreakTime = 15
        val newLongBreakInterval = 6
        pomodoroViewModel.startPomodoro()
        fakeTimer.nextTick()
        pomodoroViewModel.updateSettings(
            PomodoroSettings(
                pomodoroTime = newPomodoroTime,
                breakTime = newBreakTime,
                longBreakTime = newLongBreakTime,
                longBreakInterval = newLongBreakInterval
            )
        )
        state = pomodoroViewModel.uiState.value
        assertEquals("Start ${EPomodoroButtonText.POMODORO.buttonText}", state.buttonText)
        assertEquals("50:00", state.timeText)
    }

    @Test
    fun onUpdateSettingsWhileBreakIsCounting_OnUpdatePomodoroSettings_ButtonTextIsStartAndTimeTextIsBreak() {
        val newPomodoroTime = 50
        val newBreakTime = 10
        val newLongBreakTime = 15
        val newLongBreakInterval = 6
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        pomodoroViewModel.startPomodoro()
        fakeTimer.nextTick()
        pomodoroViewModel.updateSettings(
            PomodoroSettings(
                pomodoroTime = newPomodoroTime,
                breakTime = newBreakTime,
                longBreakTime = newLongBreakTime,
                longBreakInterval = newLongBreakInterval
            )
        )
        state = pomodoroViewModel.uiState.value
        assertEquals("Start ${EPomodoroButtonText.BREAK.buttonText}", state.buttonText)
        assertEquals("10:00", state.timeText)
    }

    @Test
    fun timeTextWhileBreakAfterUpdatedSettings_OnSecondPassed_TimeTextHasNewTime() {
        val newPomodoroTime = 50
        val newBreakTime = 10
        val newLongBreakTime = 15
        val newLongBreakInterval = 6
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        pomodoroViewModel.updateSettings(
            PomodoroSettings(
                pomodoroTime = newPomodoroTime,
                breakTime = newBreakTime,
                longBreakTime = newLongBreakTime,
                longBreakInterval = newLongBreakInterval
            )
        )
        pomodoroViewModel.startPomodoro()
        fakeTimer.nextTick()
        state = pomodoroViewModel.uiState.value
        assertEquals("09:59", state.timeText)
    }

    @Test
    fun timeText_finishedPomodoros_UpdateLongBreakStateOnFinish() {
        val newPomodoroTime = 1
        val newBreakTime = 1
        val newLongBreakTime = 1
        val newLongBreakInterval = 1
        pomodoroViewModel.updateSettings(
            PomodoroSettings(
                pomodoroTime = newPomodoroTime,
                breakTime = newBreakTime,
                longBreakTime = newLongBreakTime,
                longBreakInterval = newLongBreakInterval
            )
        )
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        state = pomodoroViewModel.uiState.value
        assertFalse(state.counting)
        assertEquals("Start ${EPomodoroButtonText.LONG_BREAK.buttonText}", state.buttonText)
        assertEquals(1, state.finishedPomodoros)
    }

    @Test
    fun pomodoroState_StartedLongBreakState_UpdateLongBreakState() {
        val newPomodoroTime = 1
        val newBreakTime = 1
        val newLongBreakTime = 1
        val newLongBreakInterval = 1
        pomodoroViewModel.updateSettings(
            PomodoroSettings(
                pomodoroTime = newPomodoroTime,
                breakTime = newBreakTime,
                longBreakTime = newLongBreakTime,
                longBreakInterval = newLongBreakInterval
            )
        )
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        pomodoroViewModel.startPomodoro()
        state = pomodoroViewModel.uiState.value
        assertTrue(state.counting)
        assertEquals(
            "Stop ${EPomodoroButtonText.LONG_BREAK.buttonText}",
            state.buttonText
        )
        assertEquals(1, state.finishedPomodoros)
    }

    @Test
    fun pomodoroState_FinishedLongBreakState_ResetPomodoroState() {
        val newPomodoroTime = 1
        val newBreakTime = 1
        val newLongBreakTime = 1
        val newLongBreakInterval = 1
        pomodoroViewModel.updateSettings(
            PomodoroSettings(
                pomodoroTime = newPomodoroTime,
                breakTime = newBreakTime,
                longBreakTime = newLongBreakTime,
                longBreakInterval = newLongBreakInterval
            )
        )
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        state = pomodoroViewModel.uiState.value
        assertFalse(state.counting)
        assertEquals(
            "Start ${EPomodoroButtonText.POMODORO.buttonText}",
            state.buttonText
        )
        assertEquals(0, state.finishedPomodoros)
    }
}
package com.example.taskpomodoro

import com.example.taskpomodoro.ui.PomodoroViewModel
import com.example.taskpomodoro.model.Timer
import org.junit.Test

import org.junit.Assert.*

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
    private lateinit var pomodoroViewModel: PomodoroViewModel
    override fun playTimer() {}

    override fun stopTimer() {}

    override fun attach(pomodoroViewModel: PomodoroViewModel) {
        this.pomodoroViewModel = pomodoroViewModel
    }

    fun nextTick() {
        actualTimeInMillis -= 1000
        pomodoroViewModel.updateTimeText(actualTimeInMillis)
    }

    fun goToFinish() {
        actualTimeInMillis = 0
        pomodoroViewModel.updateStateOnFinish()
    }
}

class PomodoroViewModelTest {
    private val dummyMillis: Long = 1000*25*60
    private val fakeTimer: FakeTimer = FakeTimer(dummyMillis)
    private val pomodoroViewModel = PomodoroViewModel(fakeTimer)
    private var state = pomodoroViewModel.uiState.value

    @Test
    fun viewModelConstructor_Build_NonNullTimerSet() {
        val pomodoroWithoutTimer = PomodoroViewModel()
        assertNotNull(pomodoroWithoutTimer.uiState.value.timer)
    }

    @Test
    fun initialUiState_Construct_NonStartingState() {
        assertEquals("25:00", state.timeText)
        assertEquals(PomodoroViewModel.ButtonText.START.buttonText, state.buttonText)
        assertFalse(state.counting)
    }

    @Test
    fun onStartTimerState_StartTimer_StartCountingAndButtonText() {
        pomodoroViewModel.startPomodoro()
        state = pomodoroViewModel.uiState.value
        assertTrue(state.counting)
        assertEquals(PomodoroViewModel.ButtonText.STOP.buttonText, state.buttonText)
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
    fun onFinishTickState_OnFinishTimer_UpdateStateOnFinish() {
        pomodoroViewModel.startPomodoro()
        fakeTimer.goToFinish()
        state = pomodoroViewModel.uiState.value
        assertFalse(state.counting)
        assertEquals(PomodoroViewModel.ButtonText.START.buttonText, state.buttonText)
        assertEquals("25:00", state.timeText)
    }

}
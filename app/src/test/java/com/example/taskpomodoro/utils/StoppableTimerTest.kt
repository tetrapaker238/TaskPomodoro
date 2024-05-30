package com.example.taskpomodoro.utils
import org.junit.Test

class StoppableTimerTest {

    private val stoppableCountDownTimer = object: StoppableCountDownTimer(1000, 1000) {
        override fun onTimerTick(millisUntilFinished: Long) {}
        override fun onTimerFinish() {}
    }

    @Test
    fun timerStop_Pause_PausedTimer() {
        //TODO: Implement time based Tests with Mockito or Roboelectric
    }

}
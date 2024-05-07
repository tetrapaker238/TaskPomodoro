package com.example.taskpomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskpomodoro.ui.theme.TaskPomodoroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskPomodoroTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TimeDisplay()
                }
            }
        }
    }
}

@Composable
fun TimeDisplay() {
    ButtonAndTime(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
    )
}

@Composable
fun ButtonAndTime(modifier: Modifier = Modifier) {
    val wholePomodoroInMs: Long = 1000*60*25
    var timeText by rememberSaveable { mutableStateOf("25:00") }
    var buttonText by rememberSaveable { mutableStateOf("Start pomodoro") }
    var lastTimeInMs by rememberSaveable { mutableLongStateOf(wholePomodoroInMs) }
    var timer: CountDownTimer? by rememberSaveable { mutableStateOf(null) }
    var started by rememberSaveable { mutableStateOf(false) }
    val initTimer = fun(timeInMs: Long): CountDownTimer {
        return object: CountDownTimer(timeInMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                lastTimeInMs = millisUntilFinished
                val minutes = (millisUntilFinished / (1000 * 60)).toInt()
                val seconds = ((millisUntilFinished - (minutes * 1000 * 60)) / 1000).toInt()
                val strMinutes = if (minutes >= 10) minutes.toString() else "0$minutes"
                val strSeconds = if (seconds >= 10) seconds.toString() else "0$seconds"
                timeText = "$strMinutes:$strSeconds"
            }

            override fun onFinish() {
                timeText = "25:00"
                buttonText = "Start pomodoro"
            }
        }.start()
    }

    fun startTimer() {
        if (lastTimeInMs.toInt() == 0) {
            lastTimeInMs = wholePomodoroInMs
        }
        timer = initTimer(lastTimeInMs)
        started = true
        buttonText = "Stop pomodoro"
    }

    fun stopTimer() {
        if (timer?.equals(null) == false) {
            timer!!.cancel()
            started = false
            buttonText = "Start pomodoro"
        }
    }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = timeText,
        )
        Button(onClick = {
            if (!started){
                startTimer()
            } else {
                stopTimer()
            }
        }) {
            Text(text = buttonText)
        }
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GreetingPreview() {
    TaskPomodoroTheme {
        TimeDisplay()
    }
}
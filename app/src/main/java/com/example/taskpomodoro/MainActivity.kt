package com.example.taskpomodoro

import android.os.Bundle
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.taskpomodoro.ui.PomodoroViewModel
import com.example.taskpomodoro.ui.theme.TaskPomodoroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val player = ExoPlayer.Builder(this).build()
        val playerView = PlayerView(this)
        playerView.player = player
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
fun ButtonAndTime(modifier: Modifier = Modifier, pomodoroViewModel: PomodoroViewModel = viewModel()) {

    val pomodoroUiState by pomodoroViewModel.uiState.collectAsState() // Delegates from the
    // pomodoroViewModel Ui State the collectAsState getter

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = pomodoroUiState.timeText,
        )
        Button(onClick = {
            if (!pomodoroUiState.counting){
                pomodoroViewModel.startPomodoro()
            } else {
                pomodoroViewModel.stopPomodoro()
            }
        }) {
            Text(
                text = pomodoroUiState.buttonText,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true,

)
@Composable
fun GreetingPreview() {
    TaskPomodoroTheme (darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TimeDisplay()
        }
    }
}
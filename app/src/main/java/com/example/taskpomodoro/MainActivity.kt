package com.example.taskpomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.taskpomodoro.ui.PomodoroSettings
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
fun Toolbar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        IconButton(onClick = {}) {
            Icon(Icons.Filled.Settings,
                contentDescription = "Configure pomodoro",
                tint = MaterialTheme.colorScheme.onPrimary
            )
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

@Composable
fun PomodoroScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.Top) {
            Toolbar()
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimeDisplay()
        }
    }
}

@Composable
fun SettingsForm(modifier: Modifier = Modifier, initialSettings: PomodoroSettings, updateState: (PomodoroSettings) -> Unit) {
    var pomodoroTime by remember { mutableStateOf(initialSettings.pomodoroTime.toString()) }
    var breakTime by remember { mutableStateOf(initialSettings.breakTime.toString()) }
    var longBreakTime by remember { mutableStateOf(initialSettings.longBreakTime.toString()) }
    var longBreakInterval by remember { mutableStateOf(initialSettings.longBreakInterval.toString())}
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(value= pomodoroTime, onValueChange =
            {valor: String -> pomodoroTime = valor},
            label = {Text("Pomodoro")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.height(26.dp))
        TextField(value= breakTime, onValueChange = {valor: String -> breakTime =  valor} , label = {Text("Break")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) )
        Spacer(modifier = Modifier.height(26.dp))
        TextField(value= longBreakTime, onValueChange = {valor: String -> longBreakTime =  valor} , label = {Text("Long break")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) )
        Spacer(modifier = Modifier.height(26.dp))
        TextField(value= longBreakInterval, onValueChange = {valor: String -> longBreakInterval =  valor} , label = {Text("Long break interval")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) )
        Spacer(modifier = Modifier.height(52.dp))
        Button(onClick =  {updateState(PomodoroSettings(
            pomodoroTime = pomodoroTime.toInt(),
            breakTime = breakTime.toInt(),
            longBreakTime = longBreakTime.toInt(),
            longBreakInterval = longBreakInterval.toInt()
        ))}) {
            Text("ACCEPT")
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun SettingsFormPreview() {
    TaskPomodoroTheme (darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SettingsForm(initialSettings = PomodoroSettings(25, 5, 10, 4), updateState = {})
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
            PomodoroScreen()
        }
    }
}
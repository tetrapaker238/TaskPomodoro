package com.example.taskpomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.taskpomodoro.ui.PomodoroSettings
import com.example.taskpomodoro.ui.PomodoroUiState
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
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    PomodoroScreen()
                }
            }
        }
    }
}

@Composable
fun Toolbar(modifier: Modifier = Modifier, onSettingsClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(
                WindowInsets.statusBars
            ), horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onSettingsClick) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Configure pomodoro",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun TimeDisplay(pomodoroViewModel: PomodoroViewModel) {
    ButtonAndTime(
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center),
        pomodoroViewModel = pomodoroViewModel
    )
}

@Composable
fun FinishedPomodoroText(pomodoroViewModel: PomodoroViewModel) {
    val pomodoroUiState by pomodoroViewModel.uiState.collectAsState()
    Text(
        text = pomodoroUiState.finishedPomodoros.toString()
                + " / "
                + pomodoroUiState.pomodoroSettings.longBreakInterval.toString(),
    )
}

@Composable
fun ButtonAndTime(
    modifier: Modifier = Modifier, pomodoroViewModel: PomodoroViewModel
) {

    val pomodoroUiState by pomodoroViewModel.uiState.collectAsState()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = pomodoroUiState.timeText,
        )
        Button(onClick = {
            if (!pomodoroUiState.counting) {
                pomodoroViewModel.startPomodoro()
            } else {
                pomodoroViewModel.stopPomodoro()
            }
        }) {
            Text(
                text = pomodoroUiState.buttonText, style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier, pomodoroViewModel: PomodoroViewModel = viewModel()
) {
    val pomodoroUiState: PomodoroUiState by pomodoroViewModel.uiState.collectAsState()
    // pomodoroViewModel Ui State the collectAsState getter
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Toolbar(
            modifier = Modifier.align(Alignment.TopCenter),
            onSettingsClick = { pomodoroViewModel.toggleDialog() }
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TimeDisplay(pomodoroViewModel = pomodoroViewModel)
            Spacer(modifier = Modifier.height(16.dp))
            FinishedPomodoroText(pomodoroViewModel = pomodoroViewModel)
        }


        if (pomodoroUiState.showDialog) {
            DialogWithForm(
                initialSettings = pomodoroUiState.pomodoroSettings,
                updateState = { newSettings ->
                    pomodoroViewModel.updateSettings(newSettings)
                    pomodoroViewModel.toggleDialog()
                },
                onDismissRequest = { pomodoroViewModel.toggleDialog() })
        }
    }

}

@Composable
fun NumericTextFieldWithFilter(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    TextField(
        value = value,
        onValueChange = { newText ->
            if (newText.all { it.isDigit() }) {
                onValueChange(newText)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@Composable
fun SettingsForm(
    modifier: Modifier = Modifier,
    initialSettings: PomodoroSettings,
    updateState: (PomodoroSettings) -> Unit
) {
    var pomodoroTime by remember { mutableStateOf(initialSettings.pomodoroTime.toString()) }
    var breakTime by remember { mutableStateOf(initialSettings.breakTime.toString()) }
    var longBreakTime by remember { mutableStateOf(initialSettings.longBreakTime.toString()) }
    var longBreakInterval by remember { mutableStateOf(initialSettings.longBreakInterval.toString()) }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        NumericTextFieldWithFilter(
            value = pomodoroTime,
            onValueChange = { pomodoroTime = it },
            label = "Pomodoro"
        )
        Spacer(modifier = Modifier.height(26.dp))
        NumericTextFieldWithFilter(
            value = breakTime,
            onValueChange = { breakTime = it },
            label = "Break"
        )
        Spacer(modifier = Modifier.height(26.dp))
        NumericTextFieldWithFilter(
            value = longBreakTime,
            onValueChange = { longBreakTime = it },
            label = "Long break"
        )
        Spacer(modifier = Modifier.height(26.dp))
        NumericTextFieldWithFilter(
            value = longBreakInterval,
            onValueChange = { longBreakInterval = it },
            label = "Long break interval"
        )
        Spacer(modifier = Modifier.height(52.dp))
        Button(onClick = {
            updateState(
                PomodoroSettings(
                    pomodoroTime = pomodoroTime.toIntOrNull() ?: 0,
                    breakTime = breakTime.toIntOrNull() ?: 0,
                    longBreakTime = longBreakTime.toIntOrNull() ?: 0,
                    longBreakInterval = longBreakInterval.toIntOrNull() ?: 0
                )
            )
        }) {
            Text("ACCEPT")
        }
    }
}

@Composable
fun DialogWithForm(
    modifier: Modifier = Modifier,
    initialSettings: PomodoroSettings,
    updateState: (PomodoroSettings) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.statusBars),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(all = 24.dp)
            ) {
                Text(text = "Pomodoro settings", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                SettingsForm(initialSettings = initialSettings, updateState = updateState)
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun SettingsFormPreview() {
    TaskPomodoroTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
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
    TaskPomodoroTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            PomodoroScreen()
        }
    }
}

package com.example.taskpomodoro.presentation.view.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.taskpomodoro.domain.dataclasses.PomodoroSettings
import com.example.taskpomodoro.ui.theme.TaskPomodoroTheme

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

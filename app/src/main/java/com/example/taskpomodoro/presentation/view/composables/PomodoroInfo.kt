package com.example.taskpomodoro.presentation.view.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.taskpomodoro.ui.PomodoroViewModel

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
fun FinishedPomodoroText(pomodoroViewModel: PomodoroViewModel) {
    val pomodoroUiState by pomodoroViewModel.uiState.collectAsState()
    Text(
        text = pomodoroUiState.finishedPomodoros.toString()
                + " / "
                + pomodoroUiState.pomodoroSettings.longBreakInterval.toString(),
    )
}

@Composable
fun TimeDisplay(pomodoroViewModel: PomodoroViewModel) {
    ButtonAndTime(
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center),
        pomodoroViewModel = pomodoroViewModel
    )
}


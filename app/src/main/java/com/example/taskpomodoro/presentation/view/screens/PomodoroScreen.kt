package com.example.taskpomodoro.presentation.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskpomodoro.ui.state.PomodoroUiState
import com.example.taskpomodoro.presentation.view.composables.DialogWithForm
import com.example.taskpomodoro.presentation.view.composables.FinishedPomodoroText
import com.example.taskpomodoro.presentation.view.composables.TaskSection
import com.example.taskpomodoro.presentation.view.composables.TimeDisplay
import com.example.taskpomodoro.presentation.viewmodel.PomodoroViewModel

@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier, pomodoroViewModel: PomodoroViewModel = viewModel(factory = PomodoroViewModel.Factory)
) {
    val pomodoroUiState: PomodoroUiState by pomodoroViewModel.uiState.collectAsState()
    
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TimeDisplay(pomodoroViewModel = pomodoroViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        FinishedPomodoroText(pomodoroViewModel = pomodoroViewModel)
        Spacer(modifier.height(24.dp))
        TaskSection()
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
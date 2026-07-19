package com.example.taskpomodoro.presentation.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskpomodoro.domain.enums.TaskImportance
import com.example.taskpomodoro.presentation.view.composables.TaskItem

@Composable
fun TasksScreen(modifier: Modifier = Modifier) {

    val scrollableState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(scrollableState)) {
        TaskItem(task = "task 1", taskImportance = TaskImportance.LOW)
        TaskItem(task = "task 2", taskImportance = TaskImportance.MEDIUM)
        TaskItem(task = "task 2", taskImportance = TaskImportance.HIGH)
        TaskItem(task = "task 2", taskImportance = TaskImportance.VERY_HIGH)
    }
}
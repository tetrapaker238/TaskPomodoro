package com.example.taskpomodoro.presentation.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskpomodoro.data.local.LocalTasksDataProvider
import com.example.taskpomodoro.domain.enums.TaskStatus
import com.example.taskpomodoro.presentation.view.composables.TaskItem

@Composable
fun TasksScreen(modifier: Modifier = Modifier) {

    val scrollableState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(scrollableState)) {
        LocalTasksDataProvider.sampleTasks.forEach {
            task -> TaskItem(
                task = task.text,
                taskImportance = task.importance,
                done = task.status == TaskStatus.DONE
            )
        }
    }
}
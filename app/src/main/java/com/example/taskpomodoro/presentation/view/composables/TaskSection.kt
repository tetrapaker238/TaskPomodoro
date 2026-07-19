package com.example.taskpomodoro.presentation.view.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskpomodoro.data.local.LocalTasksDataProvider
import com.example.taskpomodoro.domain.dataclasses.PomodoroTask
import com.example.taskpomodoro.domain.enums.TaskStatus

@Composable
fun TaskSection(modifier: Modifier = Modifier) {

    val undoneTaskList = LocalTasksDataProvider.getUndoneTasks()

    Box(
        modifier = modifier
            .border(width = 2.dp, color = Color.Black)
            .fillMaxWidth(0.75f)
            .padding(all = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Tasks", style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(thickness = 2.dp)
            TaskList(tasks = undoneTaskList)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TaskList(modifier: Modifier = Modifier, tasks: List<PomodoroTask> ) {
    Column(modifier = modifier) {
        tasks.forEach {
            task -> TaskItem(
                task = task.text,
                taskImportance = task.importance,
                done = task.status === TaskStatus.DONE
            )
        }
    }
}

@Preview
@Composable
fun TaskSectionPreview() {
    TaskSection()
}
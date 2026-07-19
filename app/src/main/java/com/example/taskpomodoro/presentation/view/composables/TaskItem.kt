package com.example.taskpomodoro.presentation.view.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskpomodoro.R
import com.example.taskpomodoro.domain.enums.TaskImportance


@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: String,
    taskImportance: TaskImportance = TaskImportance.LOW
) {

    var taskChecked by rememberSaveable { mutableStateOf(false) }
    val textDecoration = if (taskChecked) TextDecoration.LineThrough else null

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                painter = painterResource(R.drawable.circle_24px),
                contentDescription = "Task importance",
                tint = taskImportance.color
            )
            Text(
                task, style = MaterialTheme.typography.headlineSmall,
                textDecoration = textDecoration
            )
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = taskChecked,
                onCheckedChange = { newVal -> taskChecked = newVal }
            )
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true
)
@Composable
fun TaskItemPreview() {
    Column() {
        TaskItem(task = "Crear el task pomodoro")
        TaskItem(task = "Crear el task pomodoro", taskImportance = TaskImportance.MEDIUM)
        TaskItem(task = "Crear el task pomodoro", taskImportance = TaskImportance.HIGH)
        TaskItem(task = "Crear el task pomodoro", taskImportance = TaskImportance.VERY_HIGH)
    }
}
package com.example.taskpomodoro.presentation.view.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TaskSection(modifier: Modifier = Modifier) {

    val taskList = rememberSaveable { mutableStateListOf<String>() }
    var enabledTaskBar by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (!enabledTaskBar) {
            Row(modifier = Modifier.border(width = 2.dp, color=Color.Red) , verticalAlignment = Alignment.CenterVertically) {
                Text("Agregar tareas")
                IconButton(
                    onClick = {
                        enabledTaskBar = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        } else {
            Column {
                Row(modifier = Modifier.border(width = 2.dp, color=Color.Red), verticalAlignment = Alignment.CenterVertically) {
                    Text("Dejar de agregar tareas")
                    IconButton(
                        onClick = {
                            enabledTaskBar = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done adding tasks"
                        )
                    }
                }
                Spacer(modifier= Modifier.height(8.dp))
                TaskTextbar(
                ) {
                        task -> taskList.add(task)
                }
            }
        }
        TaskList(tasks = taskList.toList())
    }
}

@Composable
fun TaskList(modifier: Modifier = Modifier, tasks: List<String> ) {
    Column(modifier = modifier) {
        tasks.forEach {
            Text(it)
        }
    }
}

@Preview
@Composable
fun TaskSectionPreview() {
    TaskSection()
}
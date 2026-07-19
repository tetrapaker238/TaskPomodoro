package com.example.taskpomodoro.presentation.view.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskpomodoro.R

@Composable
fun TaskSection(modifier: Modifier = Modifier) {

    val taskList = rememberSaveable { mutableStateListOf<String>() }

    Box(
        modifier = modifier
            .border(width = 2.dp, color = Color.Black)
            .fillMaxWidth(0.75f)
            .fillMaxHeight(0.3f)
            .padding(all = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Tasks", style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(thickness = 2.dp)
            TaskList(tasks = taskList.toList())
            Spacer(modifier = Modifier.height(8.dp))
        }
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
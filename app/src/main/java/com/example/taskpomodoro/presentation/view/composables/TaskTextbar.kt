package com.example.taskpomodoro.presentation.view.composables

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TaskTextbar(modifier: Modifier = Modifier, onClickedTextBar: (String) -> Unit) {
    var text by remember { mutableStateOf("Hello") }
    TextField(
        modifier = modifier,
        //TODO: implementar el textfield. Tener en cuenta la actualización en un futuro a uso de
        // estado en vez de valor
        value = text,
        onValueChange = {
            text = it
            onClickedTextBar(it)
        },
        label = {Text("Tarea")}
    )
}

@Preview
@Composable
fun TaskTextbarPreview() {
    return TaskTextbar(onClickedTextBar = {_ -> })
}
package com.example.taskpomodoro.presentation.view.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskpomodoro.R

@Composable
fun TaskTextbar(modifier: Modifier = Modifier, onTaskAdded: (String) -> Unit = {}) {
    var innerText by rememberSaveable { mutableStateOf("") }
    TextField(
        modifier = modifier,
        //TODO: implementar el textfield. Tener en cuenta la actualización en un futuro a uso de
        // estado en vez de valor
        value = innerText,
        onValueChange = {
            innerText = it
        },
        singleLine = true,
        label = {Text("Tarea")},
        trailingIcon = { IconButton(
            onClick = {
                if (innerText.length > 2) {
                    onTaskAdded(innerText)
                    innerText = ""
                } //TODO: Mostrar borde rojo y mensaje que diga que debe tener al menos 3 caracteres
            }
        ) {
            Icon(painter = painterResource(R.drawable.add_24px), contentDescription = "Add Task")
        }}
    )
}

@Preview
@Composable
fun TaskTextbarPreview() {
    return TaskTextbar(onTaskAdded = { _ -> })
}
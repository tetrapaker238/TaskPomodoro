package com.example.taskpomodoro.presentation.view.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.taskpomodoro.R

@Composable
fun NavigationButtons(modifier: Modifier = Modifier) {

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        val navigationButtonModifier = Modifier
            .weight(1f)
            .fillMaxHeight()
        NavigationButton(
            modifier = navigationButtonModifier,
            onClick = {},
            resourceId = R.drawable.alarm_on_24px,
            contentDescription = "Pomodoro section",
            label = "Pomodoro"
        )
        NavigationButton(
            modifier = navigationButtonModifier,
            onClick = {},
            resourceId = R.drawable.assignment_24px,
            contentDescription = "Task section",
            label = "Tasks"
        )
        NavigationButton(
            modifier = navigationButtonModifier,
            onClick = {},
            resourceId = R.drawable.calendar_month_24px,
            contentDescription = "Calendar section",
            label = "Calendar"
        )
    }
}

@Composable
fun NavigationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes resourceId: Int,
    contentDescription: String,
    label: String
) {
    IconButton(modifier = modifier, onClick = onClick, shape = RectangleShape) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painter = painterResource(resourceId), contentDescription)
            Text(label,style = MaterialTheme.typography.labelSmall , maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
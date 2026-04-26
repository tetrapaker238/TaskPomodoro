package com.example.taskpomodoro.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.taskpomodoro.R
import com.example.taskpomodoro.presentation.view.composables.NavigationButtons
import com.example.taskpomodoro.presentation.view.screens.PomodoroScreen
import com.example.taskpomodoro.presentation.view.screens.TasksScreen
import com.example.taskpomodoro.presentation.viewmodel.PomodoroViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    pomodoroViewModel: PomodoroViewModel = viewModel(factory = PomodoroViewModel.Factory)
) {
    val backStack = rememberNavBackStack(Routes.PomodoroKey(null))

    // Determine title based on current backStack key
    val currentKey = backStack.lastOrNull()
    val title = when (currentKey) {
        is Routes.PomodoroKey -> "Pomodoro timer"
        is Routes.TasksKey -> "Task management"
        else -> "Task Pomodoro"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = { Text(title) },
                actions = {
                    // Only show settings button if we are on the Pomodoro screen
                    if (currentKey is Routes.PomodoroKey) {
                        IconButton(onClick = { pomodoroViewModel.toggleDialog() }) {
                            Icon(
                                painter = painterResource(R.drawable.settings_24px),
                                contentDescription = "Configure pomodoro",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                NavigationButtons(backStack = backStack)
            }
        },
    )
    {
        innerPadding ->
        val transitionDuration = 250
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Routes.PomodoroKey> {
                    PomodoroScreen(pomodoroViewModel = pomodoroViewModel)
                }
                entry<Routes.TasksKey> {
                    TasksScreen()
                }
                entry<Routes.Error> {
                    Text("Vista no encontrada")
                }
            },
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(transitionDuration)
                ) togetherWith slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(transitionDuration))
            },
            popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(transitionDuration)
                ) togetherWith slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(transitionDuration))
            },
            predictivePopTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(transitionDuration)
                ) togetherWith slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(transitionDuration))
            },
        )
    }
}
package com.example.taskpomodoro.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.taskpomodoro.presentation.view.screens.PomodoroScreen


@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Routes.PomodoroKey(null))

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        //        entryProvider = {
//            key ->
//            when(key) {
//                is Routes.PomodoroKey -> NavEntry(key){
//                    PomodoroScreen()
//                }
//                else -> NavEntry(key = Routes.Error) {
//                    Text("Vista no encontrada :c")
//                }
//            }
//        },
        entryProvider = entryProvider {
            entry<Routes.PomodoroKey> {
                PomodoroScreen()
            }
            entry<Routes.Error> {
                Text("Vista no encontrada")
            }
        },
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(250)
            ) togetherWith slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(1000))
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(250)
            ) togetherWith slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(1000))
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(250)
            ) togetherWith slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(1000))
        },
    )

}
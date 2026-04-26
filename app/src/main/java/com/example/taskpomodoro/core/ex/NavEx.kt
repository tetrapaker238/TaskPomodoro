package com.example.taskpomodoro.core.ex

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(screen: NavKey) {
    if (screen in this) return this.backTo(screen)
    add(screen)
}

fun NavBackStack<NavKey>.back() {
    if (isEmpty()) return
    removeLastOrNull()
}

fun NavBackStack<NavKey>.backTo(targetScreen: NavKey) {
    if (isEmpty()) return
    if (targetScreen !in this) return

    while(isNotEmpty() && last() != targetScreen) {
        removeLastOrNull()
    }
}
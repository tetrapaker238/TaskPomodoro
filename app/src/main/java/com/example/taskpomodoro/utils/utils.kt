package com.example.taskpomodoro.utils

fun convertMinutesToMilliseconds(minutes: Int): Long {
    return (1000 * 60 * minutes).toLong()
}

fun getTimeFromMs(millis: Long): String {
    val minutes = (millis / (1000 * 60)).toInt()
    val seconds = ((millis - (minutes * 1000 * 60)) / 1000).toInt()
    val strMinutes = if (minutes >= 10) minutes.toString() else "0$minutes"
    val strSeconds = if (seconds >= 10) seconds.toString() else "0$seconds"
    return "$strMinutes:$strSeconds"
}
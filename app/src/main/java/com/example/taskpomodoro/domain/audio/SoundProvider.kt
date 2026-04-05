package com.example.taskpomodoro.domain.audio

interface SoundProvider {
    fun playInitSound()
    fun playStopSound()
    fun playFinishSound()
}
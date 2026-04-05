package com.example.taskpomodoro.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.taskpomodoro.domain.audio.SoundProvider

class SoundManager(context: Context): SoundProvider {

    val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(2).setAudioAttributes(
        AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(
            AudioAttributes.USAGE_MEDIA).build()).build()
    val soundIdInicio = soundPool.load(context, com.example.taskpomodoro.R.raw.inicio_pomodoro, 1)
    val soundIdPausa = soundPool.load(context, com.example.taskpomodoro.R.raw.pausa_pomodoro, 1)
    val soundIdFin = soundPool.load(context, com.example.taskpomodoro.R.raw.fin_pomodoro, 1)

    private fun playSound(soundId: Int) {
        soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
    }

    override fun playInitSound() {
        playSound(soundIdInicio)
    }

    override fun playStopSound() {
        playSound(soundIdPausa)
    }

    override fun playFinishSound() {
        playSound(soundIdFin)
    }



}
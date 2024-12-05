package com.practicum.playlistmaker.domain.mediaPlayer.api

import com.practicum.playlistmaker.domain.mediaPlayer.models.PlayerStatus

interface MediaPlayerRepository {
    fun prepare(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )
    fun start()
    fun pause()
    fun release()
    fun timerUpdate(
        onTimerUpdated: (String) -> Unit
    )
}
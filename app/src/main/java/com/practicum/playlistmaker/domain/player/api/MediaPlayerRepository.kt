package com.practicum.playlistmaker.domain.player.api

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
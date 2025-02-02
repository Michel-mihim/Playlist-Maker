package com.practicum.playlistmaker.player.domain.api

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
package com.practicum.playlistmaker.domain.mediaPlayer.api

import com.practicum.playlistmaker.domain.mediaPlayer.models.PlayerStatus

interface MediaPlayerInteractor {
    fun prepare(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
        )
    fun start()
    fun pause()
    fun release()
}
package com.practicum.playlistmaker.domain.mediaPlayer.impl

import com.practicum.playlistmaker.domain.mediaPlayer.api.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.mediaPlayer.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository): MediaPlayerInteractor {
    override fun prepare(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        mediaPlayerRepository.prepare(url, onPrepared, onCompletion)
    }

    override fun start() {
        mediaPlayerRepository.start()
    }

    override fun pause() {
        mediaPlayerRepository.pause()
    }

    override fun release() {
        mediaPlayerRepository.release()
    }

    override fun timerUpdate(
        onTimerUpdated: (String) -> Unit
    ) {
        mediaPlayerRepository.timerUpdate(onTimerUpdated)
    }

}
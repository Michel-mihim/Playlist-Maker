package com.practicum.playlistmaker.data.mediaPlayer

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.mediaPlayer.api.MediaPlayerRepository
import com.practicum.playlistmaker.domain.mediaPlayer.models.PlayerStatus
import com.practicum.playlistmaker.utils.constants.Constants

class MediaPlayerRepositoryImpl(val mediaPlayer: MediaPlayer): MediaPlayerRepository {
    override fun prepare(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }

        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

}
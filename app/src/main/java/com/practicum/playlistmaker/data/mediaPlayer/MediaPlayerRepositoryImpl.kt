package com.practicum.playlistmaker.data.mediaPlayer

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.mediaPlayer.api.MediaPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

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

    override fun timerUpdate(
        onTimerUpdated: (String) -> Unit
    ) {
        onTimerUpdated(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
    }

}
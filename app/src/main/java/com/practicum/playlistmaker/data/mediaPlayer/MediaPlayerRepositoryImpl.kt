package com.practicum.playlistmaker.data.mediaPlayer

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.mediaPlayer.api.MediaPlayerRepository
import com.practicum.playlistmaker.domain.mediaPlayer.models.PlayerStatus
import com.practicum.playlistmaker.utils.constants.Constants

class MediaPlayerRepositoryImpl(val mediaPlayer: MediaPlayer): MediaPlayerRepository {
    override fun prepare(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener{
            trackPlayButton.isEnabled = true
            playerStatus = PlayerStatus.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener{//окончание воспроизведения
            trackPlayButton.setImageResource(R.drawable.track_play)
            playerStatus = PlayerStatus.STATE_PREPARED
            handler.removeCallbacks(showProgressRunnable)
            trackProgress.text = Constants.TRACK_IS_OVER_PROGRESS
        }
    }

    override fun act() {
        TODO("Not yet implemented")
    }
}
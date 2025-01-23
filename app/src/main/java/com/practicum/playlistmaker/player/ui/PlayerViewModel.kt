package com.practicum.playlistmaker.player.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.models.PlayerStatus
import com.practicum.playlistmaker.search.domain.models.SearchActivityState
import com.practicum.playlistmaker.utils.constants.Constants

class PlayerViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        fun getPlayerViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()

    private val playerActivityTrackPlayButtonReadinessLiveData = MutableLiveData<Boolean>()
    fun observePlayerActivityTrackPlayButtonReadiness(): LiveData<Boolean> = playerActivityTrackPlayButtonReadinessLiveData

    fun mediaPlayerPrepare(previewUrl: String?) {
        mediaPlayerInteractor.prepare(
            previewUrl,
            onPrepared = { ->
                trackPlayButtonActivate()
                playerStatus = PlayerStatus.STATE_PREPARED
            },
            onCompletion = { -> //окончание воспроизведения
                trackPlayButton.setImageResource(R.drawable.track_play)
                playerStatus = PlayerStatus.STATE_PREPARED
                handler.removeCallbacks(showProgressRunnable)
                trackProgress.text = Constants.TRACK_IS_OVER_PROGRESS
            }
        )
    }

    //POSTING=======================================================================================
    private fun trackPlayButtonActivate() {
        playerActivityTrackPlayButtonReadinessLiveData.postValue(true)
    }
}
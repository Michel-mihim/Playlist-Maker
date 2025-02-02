package com.practicum.playlistmaker.player.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
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

    private var playerStatus: PlayerStatus = PlayerStatus.STATE_DEFAULT

    private val playerActivityPlayerReadinessLiveData = MutableLiveData<Boolean>()
    fun observePlayerActivityPlayerReadiness(): LiveData<Boolean> = playerActivityPlayerReadinessLiveData

    private val playerActivityPlayerStatusLiveData = MutableLiveData<PlayerStatus>()
    fun observePlayerActivityPlayerStatus(): LiveData<PlayerStatus> = playerActivityPlayerStatusLiveData

    private val playerActivityTrackProgressLiveData = MutableLiveData<String>()
    fun observePlayerActivityTrackProgress(): LiveData<String> = playerActivityTrackProgressLiveData

    private val handler = Handler(Looper.getMainLooper())
    private val showProgressRunnable = Runnable { showProgress() }

    //LIFE_CYCLE====================================================================================
    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(showProgressRunnable)
        mediaPlayerInteractor.release()
    }

    //PLAYBACK======================================================================================
    fun mediaPlayerPrepare(previewUrl: String?) {
        mediaPlayerInteractor.prepare(
            previewUrl,
            onPrepared = { ->
                trackPlayButtonActivate()
                playerStatus = PlayerStatus.STATE_PREPARED
            },
            onCompletion = { -> //окончание воспроизведения
                playerStatus = PlayerStatus.STATE_PREPARED
                trackPlayButtonRenderStatus(PlayerStatus.STATE_PREPARED)
                handler.removeCallbacks(showProgressRunnable)
                trackRenderProgress(Constants.TRACK_IS_OVER_PROGRESS)
            }
        )
    }

    fun playbackControl() {
        when (playerStatus) {
            PlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStatus.STATE_PREPARED -> {
                startPlayer()
            }
            PlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }
            PlayerStatus.STATE_DEFAULT -> TODO()
        }
    }

    private fun startPlayer(){
        mediaPlayerInteractor.start()
        playerStatus = PlayerStatus.STATE_PLAYING
        trackPlayButtonRenderStatus(PlayerStatus.STATE_PLAYING)
        handler.post(showProgressRunnable)
    }

    fun pausePlayer(){
        mediaPlayerInteractor.pause()
        playerStatus = PlayerStatus.STATE_PAUSED
        trackPlayButtonRenderStatus(PlayerStatus.STATE_PAUSED)
        handler.removeCallbacks(showProgressRunnable)
    }

    private fun showProgress(){
        mediaPlayerInteractor.timerUpdate(
            onTimerUpdated = { progress ->
                trackRenderProgress(progress)
            }
        )
        handler.postDelayed(showProgressRunnable, Constants.SHOW_PROGRESS_DELAY)
    }

    //POSTING=======================================================================================
    private fun trackPlayButtonActivate() {
        playerActivityPlayerReadinessLiveData.postValue(true)
    }

    private fun trackPlayButtonRenderStatus(status: PlayerStatus) {
        playerActivityPlayerStatusLiveData.postValue(status)
    }

    private fun trackRenderProgress(progress: String) {
        playerActivityTrackProgressLiveData.postValue(progress)
    }
}
package com.practicum.playlistmaker.player.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayerActivityState
import com.practicum.playlistmaker.player.domain.models.PlayerStatus
import com.practicum.playlistmaker.search.domain.models.SearchActivityState
import com.practicum.playlistmaker.utils.constants.Constants

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
): ViewModel() {

    private var playerStatus: PlayerStatus = PlayerStatus.STATE_DEFAULT

    private var currentProgress: String = ""

    private val playerActivityCurrentStateLiveData = MutableLiveData<PlayerActivityState>()
    fun observePlayerActivityCurrentState(): LiveData<PlayerActivityState> = playerActivityCurrentStateLiveData

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
                playerActivityPostState(
                    PlayerActivityState(
                        true,
                        PlayerStatus.STATE_PREPARED,
                        ""
                    )
                )
                playerStatus = PlayerStatus.STATE_PREPARED
            },
            onCompletion = { -> //окончание воспроизведения
                playerStatus = PlayerStatus.STATE_PREPARED
                handler.removeCallbacks(showProgressRunnable)
                playerActivityPostState(
                    PlayerActivityState(
                        true,
                        PlayerStatus.STATE_PREPARED,
                        Constants.TRACK_IS_OVER_PROGRESS
                    )
                )
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
            PlayerStatus.STATE_DEFAULT -> {}
        }
    }

    private fun startPlayer(){
        mediaPlayerInteractor.start()
        playerStatus = PlayerStatus.STATE_PLAYING
        playerActivityPostState(
            PlayerActivityState(
                true,
                PlayerStatus.STATE_PLAYING,
                currentProgress
            )
        )
        handler.post(showProgressRunnable)
    }

    fun pausePlayer(){
        mediaPlayerInteractor.pause()
        playerStatus = PlayerStatus.STATE_PAUSED
        playerActivityPostState(
            PlayerActivityState(
                true,
                PlayerStatus.STATE_PAUSED,
                currentProgress
            )
        )
        handler.removeCallbacks(showProgressRunnable)
    }

    private fun showProgress(){
        mediaPlayerInteractor.timerUpdate(
            onTimerUpdated = { progress ->
                playerActivityPostState(
                    PlayerActivityState(
                        true,
                        PlayerStatus.STATE_PLAYING,
                        progress
                    )
                )
                currentProgress = progress
            }
        )
        handler.postDelayed(showProgressRunnable, Constants.SHOW_PROGRESS_DELAY)
    }

    //POSTING=======================================================================================

    private fun playerActivityPostState(playerActivityState: PlayerActivityState) {
        playerActivityCurrentStateLiveData.postValue(playerActivityState)
    }

}
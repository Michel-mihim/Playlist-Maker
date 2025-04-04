package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.utils.constants.Constants
import com.practicum.playlistmaker.search.domain.OnHistoryUpdatedListener
import com.practicum.playlistmaker.search.domain.api.GetPlayerIntentUseCase
import com.practicum.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.practicum.playlistmaker.search.domain.models.SearchActivityNavigationState
import com.practicum.playlistmaker.search.domain.models.SearchActivityState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.classes.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val historyTracksInteractor: HistoryTracksInteractor,
    private val getPlayerIntentUseCase: GetPlayerIntentUseCase
): ViewModel() {

    private val onHistoryUpdatedListener = OnHistoryUpdatedListener {
        searchActivityNavigate()
    }

    init {
        historyTracksInteractor.setOnHistoryUpdatedListener(onHistoryUpdatedListener)
    }

    private var searchJob: Job? = null

    private var latestSearchText: String? = null

    private var searchActivityNavigationState: SearchActivityNavigationState = SearchActivityNavigationState.HISTORY

    private val tracksRecyclerList = ArrayList<Track>()
    private var historyTracks = ArrayList<Track>()

    private val searchActivityStateLiveData = MutableLiveData<SearchActivityState>()
    fun observeSearchActivityState(): LiveData<SearchActivityState> = searchActivityStateLiveData

    private val searchActivityToastStateLiveData = SingleLiveEvent<String>()
    fun observeSearchActivityToastState(): LiveData<String> = searchActivityToastStateLiveData

    private val playerActivityIntentLiveData = SingleLiveEvent<Intent>()
    fun observePlayerActivityIntent(): LiveData<Intent> = playerActivityIntentLiveData

    //NAVIGATION====================================================================================
    private fun searchActivityNavigate() {
        when (this.searchActivityNavigationState) {
            SearchActivityNavigationState.HISTORY -> showHistory()
            SearchActivityNavigationState.SEARCH_RESULT -> {}
        }
    }

    //SEARCH========================================================================================
    fun searchDelayed(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(Constants.SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun searchForce(text: String) {
        this.latestSearchText = text

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchRequest(text)
        }
    }

    private fun searchRequest(newSearchText: String){
        if (newSearchText.isNotEmpty()) {
            renderState(SearchActivityState.Loading)
            tracksRecyclerList.clear()
            searchTracksInteractor.searchTracks(newSearchText, object : SearchTracksInteractor.TracksConsumer {
                override fun consume(result: SearchTracksResult<List<Track>>) {
                    searchActivityNavigationState = SearchActivityNavigationState.SEARCH_RESULT

                    when (result) {

                        is SearchTracksResult.Success -> {
                            tracksRecyclerList.addAll(result.tracks)
                            renderState(SearchActivityState.Content(tracksRecyclerList))
                        }

                        is SearchTracksResult.Empty -> {
                            tracksRecyclerList.addAll(result.tracks)
                            renderState(SearchActivityState.Empty)
                            showToastState(Constants.TRACKS_NOT_FOUND_2)
                        }

                        is SearchTracksResult.Failure -> {
                            tracksRecyclerList.addAll(result.tracks)
                            renderState(SearchActivityState.Error)
                            showToastState("Код ошибки: ${result.code}")
                        }

                    }
                }
            })
        }
    }

    //HISTORY=======================================================================================
    fun showHistory() {
        searchActivityNavigationState = SearchActivityNavigationState.HISTORY

        historyTracks.clear()
        historyTracks.addAll(historyTracksInteractor.getTracks())
        if (historyTracks.isNotEmpty()) {
            renderState(SearchActivityState.History(historyTracks))
        } else renderState(SearchActivityState.Default)
    }

    fun writeHistory(trackClicked: Track) {
        historyTracksInteractor.addTrack(trackClicked)
    }

    fun clearHistory() {
        historyTracksInteractor.clearTracks()
    }

    //POSTING=======================================================================================
    private fun renderState(state: SearchActivityState) {
        searchActivityStateLiveData.postValue(state)
    }

    private fun showToastState(message: String) {
        searchActivityToastStateLiveData.postValue(message)
    }

    //PLAYER========================================================================================
    fun getPlayerIntent(track: Track) {
        getPlayerIntentUseCase.execute(
            track,
            onPlayerIntentReady = { intent ->
                startPlayerActivity(intent as Intent)
            }
        )
    }

    private fun startPlayerActivity(intent: Intent) {
        playerActivityIntentLiveData.postValue(intent)
    }

}
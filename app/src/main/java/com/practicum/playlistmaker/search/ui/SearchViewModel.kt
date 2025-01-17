package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.utils.constants.Constants
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.practicum.playlistmaker.search.domain.models.SearchActivityState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.classes.SingleLiveEvent

class SearchViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        fun getSearchViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer { SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val searchTracksInteractor = Creator.provideSearchTracksInteractor()
    private val historyTracksInteractor = Creator.provideHistoryTracksInteractor(getApplication<Application>())

    private val handler = Handler(Looper.getMainLooper())

    private var latestSearchText: String? = null

    private val tracksRecyclerList = ArrayList<Track>()

    private val searchRunnable = Runnable {
        val newSearchText = latestSearchText ?: ""
        searchRequest(newSearchText)
    }

    private val searchActivityStateLiveData = MutableLiveData<SearchActivityState>()
    fun observeSearchActivityState(): LiveData<SearchActivityState> = searchActivityStateLiveData

    private val searchActivityToastStateLiveData = SingleLiveEvent<String>()
    fun observeSearchActivityToastState(): LiveData<String> = searchActivityToastStateLiveData

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    fun searchDelayed(changedText: String){
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        handler.removeCallbacks(searchRunnable) // runnable - fun searchRequest()
        handler.postDelayed(searchRunnable, Constants.SEARCH_DEBOUNCE_DELAY)
    }


    private fun searchRequest(newSearchText: String){
        if (newSearchText.isNotEmpty()) {
            renderState(SearchActivityState.Loading)
            tracksRecyclerList.clear()
            searchTracksInteractor.searchTracks(newSearchText, object : SearchTracksInteractor.TracksConsumer {
                override fun consume(result: SearchTracksResult<List<Track>>) {
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

    private fun renderState(state: SearchActivityState) {
        searchActivityStateLiveData.postValue(state)
    }

    private fun showToastState(message: String) {
        searchActivityToastStateLiveData.postValue(message)
    }
}
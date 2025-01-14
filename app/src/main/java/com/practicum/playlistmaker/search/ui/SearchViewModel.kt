package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.models.SearchStatus
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.utils.constants.Constants
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY

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

    private val searchRunnable = Runnable {
        val newSearchText = latestSearchText ?: ""
        searchRequest(newSearchText)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    fun searchDebounce(changedText: String){
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        handler.removeCallbacks(searchRunnable) // runnable - fun searchRequest()
        handler.postDelayed(searchRunnable, Constants.SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(newSearchText: String){
        if (newSearchText.isNotEmpty()) {
            searchStatus = SearchStatus.SEARCH_RESULT_WAITING
            viewsVisibilityControl()
            tracks.clear()
            searchTracksInteractor.searchTracks(searchEdittext.text.toString(), object : SearchTracksInteractor.TracksConsumer {
                override fun consume(result: SearchTracksResult) {
                    handler.post{
                        when (result) {
                            is SearchTracksResult.Success -> {
                                tracks.addAll(result.tracks)
                                searchStatus = SearchStatus.TRACKS_FOUND
                                showStatus(searchStatus, Constants.SEARCH_SUCCESS)
                            }
                            is SearchTracksResult.Empty -> {
                                tracks.addAll(result.tracks)
                                searchStatus = SearchStatus.TRACKS_NOT_FOUND
                                showStatus(searchStatus, Constants.TRACKS_NOT_FOUND_2)
                            }
                            is SearchTracksResult.Failure -> {
                                tracks.addAll(result.tracks)
                                searchStatus = SearchStatus.ERROR_OCCURRED
                                showStatus(searchStatus,"Код ошибки: ${result.code}")
                            }
                        }
                        searchRecyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                        viewsVisibilityControl()
                    }
                }
            })
        }
    }
}
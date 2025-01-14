package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.models.SearchStatus
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.utils.constants.Constants

class SearchViewModel(application: Application): AndroidViewModel(application) {

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
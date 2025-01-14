package com.practicum.playlistmaker.search.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.practicum.playlistmaker.creator.Creator

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val searchTracksInteractor = Creator.provideSearchTracksInteractor()
    private val historyTracksInteractor = Creator.provideHistoryTracksInteractor(getApplication<Application>())
}
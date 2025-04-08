package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(
    private val repository: SearchTracksRepository
): SearchTracksInteractor {

    override fun searchTracks(expression: String): Flow<SearchTracksResult<List<Track>>> {
        return repository.searchTracks(expression)
    }
}
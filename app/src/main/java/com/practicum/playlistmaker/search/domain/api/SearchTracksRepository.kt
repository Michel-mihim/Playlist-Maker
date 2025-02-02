package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.search.domain.models.Track

interface SearchTracksRepository {
    fun searchTracks(
        expression: String
    ): SearchTracksResult<List<Track>>
}
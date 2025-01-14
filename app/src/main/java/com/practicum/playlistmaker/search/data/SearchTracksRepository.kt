package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.models.SearchTracksResult

interface SearchTracksRepository {
    fun searchTracks(
        expression: String
    ): SearchTracksResult
}
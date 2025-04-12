package com.practicum.playlistmaker.search.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
}
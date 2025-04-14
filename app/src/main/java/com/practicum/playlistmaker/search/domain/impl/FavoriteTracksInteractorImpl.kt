package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.db.FavoriteTracksInteractor
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {

    override suspend fun addTrack(track: Track) {
        favoriteTracksRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteTracksRepository.deleteTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getTracks()
    }
}
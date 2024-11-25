package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.searchTracks.TracksRepositoryImpl
import com.practicum.playlistmaker.data.searchTracks.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.searchTracks.api.TracksInteractor
import com.practicum.playlistmaker.domain.searchTracks.api.TracksRepository
import com.practicum.playlistmaker.domain.searchTracks.impl.TracksInteractorImpl

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun getTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}
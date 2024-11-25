package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.searchTracks.searchTracksRepositoryImpl
import com.practicum.playlistmaker.data.searchTracks.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.searchTracks.api.searchTracksInteractor
import com.practicum.playlistmaker.domain.searchTracks.api.searchTracksRepository
import com.practicum.playlistmaker.domain.searchTracks.impl.searchTracksInteractorImpl

object Creator {
    fun getTracksRepository(): searchTracksRepository {
        return searchTracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun getTracksInteractor(): searchTracksInteractor {
        return searchTracksInteractorImpl(getTracksRepository())
    }
}
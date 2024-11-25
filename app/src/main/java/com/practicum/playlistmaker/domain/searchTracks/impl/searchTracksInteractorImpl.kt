package com.practicum.playlistmaker.domain.searchTracks.impl

import com.practicum.playlistmaker.domain.searchTracks.api.searchTracksInteractor
import com.practicum.playlistmaker.domain.searchTracks.api.searchTracksRepository
import java.util.concurrent.Executors

class searchTracksInteractorImpl(private val repository: searchTracksRepository): searchTracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: searchTracksInteractor.TracksConsumer) {

        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}
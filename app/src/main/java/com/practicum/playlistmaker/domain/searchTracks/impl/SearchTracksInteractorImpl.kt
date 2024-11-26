package com.practicum.playlistmaker.domain.searchTracks.impl

import com.practicum.playlistmaker.domain.searchTracks.api.SearchTracksInteractor
import com.practicum.playlistmaker.domain.searchTracks.api.SearchTracksRepository
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(private val repository: SearchTracksRepository): SearchTracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchTracksInteractor.TracksConsumer) {

        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}
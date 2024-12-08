package com.practicum.playlistmaker.domain.history.impl

import com.practicum.playlistmaker.domain.history.OnHistoryUpdatedListener
import com.practicum.playlistmaker.domain.history.api.HistoryTracksInteractor
import com.practicum.playlistmaker.domain.history.api.HistoryTracksRepository
import com.practicum.playlistmaker.domain.searchTracks.models.Track

class HistoryTracksInteractorImpl(private val repository: HistoryTracksRepository) : HistoryTracksInteractor {

    override fun getTracks(): Array<Track> {
        return repository.getTracks()
    }

    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun clearTracks() {
        repository.clearTracks()
    }

    override fun SetOnHistoryUpdatedListener(onHistoryUpdatedListener: OnHistoryUpdatedListener) {
        repository.SetOnHistoryUpdatedListener(onHistoryUpdatedListener)
    }

}
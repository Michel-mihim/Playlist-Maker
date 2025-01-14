package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.OnHistoryUpdatedListener
import com.practicum.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.practicum.playlistmaker.search.data.HistoryTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryTracksInteractorImpl(private val repository: HistoryTracksRepository) :
    HistoryTracksInteractor {

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
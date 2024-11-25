package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.models.Track

sealed class SearchTracksResult(val tracks: List<Track>, val code: Int) {
    class Success(tracks: List<Track>, code: Int): SearchTracksResult(tracks, code){}
    class Empty(tracks: List<Track>, code: Int): SearchTracksResult(tracks, code){}
    class Failure(tracks: List<Track>, code: Int): SearchTracksResult(tracks, code){}
}
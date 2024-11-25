package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.models.Track

sealed class SearchTracksResult(val code: Int) {
    class Success(tracks: List<Track>){}
    class Empty(tracks: List<Track>){}
    class Failure(){}
}
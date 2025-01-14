package com.practicum.playlistmaker.search.domain.models

sealed interface SearchActivityState {

    object Loading: SearchActivityState

    object Default: SearchActivityState

    object Empty: SearchActivityState

    data class History(val tracks: List<Track>): SearchActivityState

    data class Content(val tracks: List<Track>): SearchActivityState

    data class Error(val errorCode: String): SearchActivityState

}
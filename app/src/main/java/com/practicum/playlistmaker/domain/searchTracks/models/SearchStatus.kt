package com.practicum.playlistmaker.domain.searchTracks.models

enum class SearchStatus {
    DEFAULT,
    SEARCH_RESULT_WAITING,
    HISTORY_PLACEHOLDER,
    TRACKS_FOUND,
    TRACKS_NOT_FOUND,
    SOMETHING_WRONG,
    ERROR_OCCURRED
}
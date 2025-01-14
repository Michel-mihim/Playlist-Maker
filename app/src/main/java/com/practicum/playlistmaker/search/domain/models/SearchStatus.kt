package com.practicum.playlistmaker.search.domain.models

enum class SearchStatus {
    DEFAULT,//default
    SEARCH_RESULT_WAITING,//loading
    HISTORY_PLACEHOLDER,//history
    TRACKS_FOUND,//content
    TRACKS_NOT_FOUND,//empty
    SOMETHING_WRONG,//error
    ERROR_OCCURRED//error
}
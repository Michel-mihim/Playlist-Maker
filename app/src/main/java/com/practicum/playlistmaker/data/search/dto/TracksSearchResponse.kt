package com.practicum.playlistmaker.data.search.dto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()

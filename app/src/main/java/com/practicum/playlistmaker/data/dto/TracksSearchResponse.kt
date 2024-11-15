package com.practicum.playlistmaker.data.dto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()

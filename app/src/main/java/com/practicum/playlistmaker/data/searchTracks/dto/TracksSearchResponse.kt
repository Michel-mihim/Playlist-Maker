package com.practicum.playlistmaker.data.searchTracks.dto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()

package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track

data class TracksSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()

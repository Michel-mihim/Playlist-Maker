package com.practicum.playlistmaker.data.searchTracks

import com.practicum.playlistmaker.data.searchTracks.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}
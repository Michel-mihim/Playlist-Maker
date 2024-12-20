package com.practicum.playlistmaker.data.searchTracks.network

import com.practicum.playlistmaker.data.searchTracks.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApiService {

    @GET("/search?entity=song")
    fun searchTracks(
        @Query("term") text: String
    ): Call<TracksSearchResponse>

}
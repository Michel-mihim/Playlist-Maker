package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {

    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String
    ): Call<TrackSearchResponse>

}
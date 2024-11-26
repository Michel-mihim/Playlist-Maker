package com.practicum.playlistmaker.data.searchTracks.network

import com.practicum.playlistmaker.data.searchTracks.NetworkClient
import com.practicum.playlistmaker.data.searchTracks.dto.Response
import com.practicum.playlistmaker.data.searchTracks.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val response = iTunesService.searchTracks(dto.expression).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = response.code() }
        } else
            return Response().apply { resultCode = 400 }
    }
}
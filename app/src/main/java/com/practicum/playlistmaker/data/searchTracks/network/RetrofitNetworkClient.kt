package com.practicum.playlistmaker.data.searchTracks.network

import android.util.Log
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
            try {
                val retrofitResp = iTunesService.searchTracks(dto.expression).execute()
                val body = retrofitResp.body() ?: Response()
                return body.apply { resultCode = retrofitResp.code() }
            }
            catch (e: Exception) {
                val body = Response().apply { resultCode = 502 }
                return body
            }
        } else
            return Response().apply { resultCode = 400 } //если запрос от нас не корректный; событие маловероятно
    }
}
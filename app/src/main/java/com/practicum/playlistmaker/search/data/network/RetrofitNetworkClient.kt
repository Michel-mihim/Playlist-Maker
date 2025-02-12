package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService
): NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            try {
                val retrofitResp = iTunesApiService.searchTracks(dto.expression).execute()
                val body = retrofitResp.body() ?: Response()
                return body.apply { resultCode = retrofitResp.code() }
            }
            catch (e: Exception) {
                val body = Response().apply { resultCode = 522 } //Connection Timed Out («соединение не отвечает»)
                return body
            }
        } else
            return Response().apply { resultCode = 400 } //если запрос от нас не корректный; событие маловероятно
    }
}
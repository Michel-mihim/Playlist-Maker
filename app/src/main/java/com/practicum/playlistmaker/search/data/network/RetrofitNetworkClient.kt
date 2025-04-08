package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService
): NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val retrofitResp = iTunesApiService.searchTracks(dto.expression)
                    retrofitResp.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else
            return Response().apply { resultCode = 400 } //если запрос от нас не корректный; событие маловероятно
    }
}
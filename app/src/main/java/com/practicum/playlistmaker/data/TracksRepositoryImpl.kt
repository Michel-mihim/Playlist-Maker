package com.practicum.playlistmaker.data

import android.util.Log
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(
        expression: String
    ): SearchTracksResult {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        if (response.resultCode == 200) {

            val tracks = (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }

            return if (tracks.isNotEmpty()) SearchTracksResult.Success(tracks, response.resultCode)
            else SearchTracksResult.Empty(emptyList(), response.resultCode)

        } else return SearchTracksResult.Failure(emptyList(), response.resultCode)
    }

}
package com.practicum.playlistmaker.data.searchTracks

import com.practicum.playlistmaker.data.searchTracks.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.searchTracks.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.searchTracks.api.SearchTracksRepository
import com.practicum.playlistmaker.domain.searchTracks.models.SearchTracksResult
import com.practicum.playlistmaker.domain.searchTracks.models.Track

class searchTracksRepositoryImpl(private val networkClient: NetworkClient) : SearchTracksRepository {

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
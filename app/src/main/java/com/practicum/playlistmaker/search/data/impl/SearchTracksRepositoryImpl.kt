package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.search.domain.models.Track

class SearchTracksRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchTracksRepository {
    override fun searchTracks(expression: String): SearchTracksResult<List<Track>> {
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

            return if (tracks.isNotEmpty()) {//not empty
                SearchTracksResult.Success(tracks, response.resultCode)
            } else {//empty
                SearchTracksResult.Empty(emptyList(), response.resultCode)
            }

        } else
        {//code!=200
            return SearchTracksResult.Failure(emptyList(), response.resultCode)
        }

    }
}
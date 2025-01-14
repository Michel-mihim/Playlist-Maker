package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.data.SearchTracksRepository
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.search.domain.models.Track

class searchTracksRepositoryImpl(private val networkClient: NetworkClient) :
    SearchTracksRepository {

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

            if (tracks.isNotEmpty()) {//not empty
                return SearchTracksResult.Success(tracks, response.resultCode)}
            else {//empty
                return SearchTracksResult.Empty(emptyList(), response.resultCode)}

        } else
        {//code!=200
            return SearchTracksResult.Failure(emptyList(), response.resultCode)
        }

    }

}
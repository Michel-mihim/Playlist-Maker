package com.practicum.playlistmaker.search.data.impl

import android.util.Log
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.db.entity.AppDatabase
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : SearchTracksRepository {
    override fun searchTracks(expression: String): Flow<SearchTracksResult<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        if (response.resultCode == 200) {
            val favoriteTrackIdList = appDatabase.trackDao().getTrackIds()
            Log.d("wtf", favoriteTrackIdList.toString())

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
                    it.previewUrl,
                    idInList(favoriteTrackIdList, it.trackId)
                )
            }

            if (tracks.isNotEmpty()) {//not empty
                emit(SearchTracksResult.Success(tracks, response.resultCode))
            } else {//empty
                emit(SearchTracksResult.Empty(emptyList(), response.resultCode))
            }

        } else
        {//code!=200
            emit(SearchTracksResult.Failure(emptyList(), response.resultCode))
        }

    }

    private fun idInList(list: List<String>, id: String): Boolean {
        return (id in list)
    }
}
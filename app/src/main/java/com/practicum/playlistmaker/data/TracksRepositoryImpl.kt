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
    ): Any {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        Log.d("WTF", "from TracksRepositoryImp code: "+response.resultCode.toString())
        Log.d("WTF", "from TracksRepositoryImp response: "+response.toString())
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
            Log.d("WTF", "from searchActivityTracksRepositoryImp tracks: "+tracks.toString())
            return tracks
        } else {
            return response.resultCode
            }
    }

}
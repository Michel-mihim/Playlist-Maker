package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.data.historyTracks.HistoryTracksRepositoryImpl
import com.practicum.playlistmaker.data.searchTracks.TracksRepositoryImpl
import com.practicum.playlistmaker.data.searchTracks.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.historyTracks.api.HistoryTracksInteractor
import com.practicum.playlistmaker.domain.historyTracks.api.HistoryTracksRepository
import com.practicum.playlistmaker.domain.historyTracks.impl.HistoryTracksInteractorImpl
import com.practicum.playlistmaker.domain.searchTracks.api.TracksInteractor
import com.practicum.playlistmaker.domain.searchTracks.api.TracksRepository
import com.practicum.playlistmaker.domain.searchTracks.impl.TracksInteractorImpl
import com.practicum.playlistmaker.utils.constants.Constants

object Creator {

    //searchTracks==================================================================================
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun getTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    //history=======================================================================================
    fun getHistoryTracksInteractor(context: Context): HistoryTracksInteractor {
        return HistoryTracksInteractorImpl(getHistoryTracksRepository(context))
    }

    private fun getHistoryTracksRepository(context: Context): HistoryTracksRepository {
        return HistoryTracksRepositoryImpl(getSharedPreferences(context))
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
    }
}
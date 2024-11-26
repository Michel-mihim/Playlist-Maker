package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.data.history.HistoryTracksRepositoryImpl
import com.practicum.playlistmaker.data.searchTracks.searchTracksRepositoryImpl
import com.practicum.playlistmaker.data.searchTracks.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.history.api.HistoryTracksInteractor
import com.practicum.playlistmaker.domain.history.api.HistoryTracksRepository
import com.practicum.playlistmaker.domain.history.impl.HistoryTracksInteractorImpl
import com.practicum.playlistmaker.domain.searchTracks.api.SearchTracksInteractor
import com.practicum.playlistmaker.domain.searchTracks.api.SearchTracksRepository
import com.practicum.playlistmaker.domain.searchTracks.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.presentation.Constants

object Creator {

    //searchTracks==================================================================================
    fun getTracksRepository(): SearchTracksRepository {
        return searchTracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun getTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }

    //history==============================================================================================

    fun getHistoryTracksInteractor(context: Context): HistoryTracksInteractor{
        return HistoryTracksInteractorImpl(getHistoryTracksRepository(context))
    }

    private fun getHistoryTracksRepository(context: Context): HistoryTracksRepository {
        return HistoryTracksRepositoryImpl(getSharedPreferences(context))
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SEARCH_HISTORY_KEY, Context.MODE_PRIVATE)
    }


}
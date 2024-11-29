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
import com.practicum.playlistmaker.utils.constants.Constants

object Creator {

    //searchTracks==================================================================================
    fun provideTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(provideTracksRepository())
    }

    private fun provideTracksRepository(): SearchTracksRepository {
        return searchTracksRepositoryImpl(RetrofitNetworkClient())
    }

    //history=======================================================================================
    fun provideHistoryTracksInteractor(context: Context): HistoryTracksInteractor{
        return HistoryTracksInteractorImpl(provideHistoryTracksRepository(context))
    }

    private fun provideHistoryTracksRepository(context: Context): HistoryTracksRepository {
        return HistoryTracksRepositoryImpl(provideSharedPreferences(context))
    }

    private fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
    }

}
package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.history.HistoryTracksRepositoryImpl
import com.practicum.playlistmaker.data.mediaPlayer.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.searchTracks.searchTracksRepositoryImpl
import com.practicum.playlistmaker.data.searchTracks.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.settings.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.history.api.HistoryTracksInteractor
import com.practicum.playlistmaker.domain.history.api.HistoryTracksRepository
import com.practicum.playlistmaker.domain.history.impl.HistoryTracksInteractorImpl
import com.practicum.playlistmaker.domain.mediaPlayer.api.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.mediaPlayer.api.MediaPlayerRepository
import com.practicum.playlistmaker.domain.mediaPlayer.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.searchTracks.api.SearchTracksInteractor
import com.practicum.playlistmaker.domain.searchTracks.api.SearchTracksRepository
import com.practicum.playlistmaker.domain.searchTracks.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.api.SettingsRepository
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.utils.constants.Constants

object Creator {
    var onProvideSharedPreferenceChangeListener: ((key: String) -> Unit)? = null

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

    /*
    fun provideSharedPreferencesChangeListener(): SharedPreferences.OnSharedPreferenceChangeListener {
        return SharedPreferences.OnSharedPreferenceChangeListener{ _, key ->
            if (key == Constants.SEARCH_HISTORY_KEY) onProvideSharedPreferenceChangeListener?.invoke(key)
        }
    }

     */

    //settings======================================================================================
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(context))
    }

    private fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(provideSharedPreferences(context))
    }

    //mediaPlayer===================================================================================
    fun provideMediaPlayerInteractor(): MediaPlayerInteractor{
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository())
    }

    private fun provideMediaPlayerRepository(): MediaPlayerRepository{
        return MediaPlayerRepositoryImpl(provideMediaPlayer())
    }

    private fun provideMediaPlayer(): MediaPlayer{
        return MediaPlayer()
    }

}
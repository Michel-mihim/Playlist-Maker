package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.playlistmaker.search.data.impl.HistoryTracksRepositoryImpl
import com.practicum.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.searchTracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.practicum.playlistmaker.search.data.HistoryTracksRepository
import com.practicum.playlistmaker.search.domain.impl.HistoryTracksInteractorImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.data.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.data.SearchTracksRepository
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.practicum.playlistmaker.utils.constants.Constants

object Creator {
    var onProvideSharedPreferenceChangeListener: ((key: String) -> Unit)? = null

    //searchTracks==================================================================================
    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(provideSearchTracksRepository())
    }

    private fun provideSearchTracksRepository(): SearchTracksRepository {
        return searchTracksRepositoryImpl(RetrofitNetworkClient())
    }

    //history=======================================================================================
    fun provideHistoryTracksInteractor(context: Context): HistoryTracksInteractor {
        return HistoryTracksInteractorImpl(provideHistoryTracksRepository(context))
    }

    private fun provideHistoryTracksRepository(context: Context): HistoryTracksRepository {
        return HistoryTracksRepositoryImpl(provideSharedPreferences(context))
    }

    private fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
    }

    //settings======================================================================================
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(context))
    }

    private fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(provideSharedPreferences(context))
    }

    //sharing=======================================================================================
    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }

    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(
            provideShareLinkIntent(),
            provideShareLinkChooser(),
            provideSupportEmailIntent(),
            provideTermsIntent()
        )
    }

    lateinit var shareLinkIntent: Intent

    private fun provideShareLinkIntent(): Intent {
        shareLinkIntent = Intent(Intent.ACTION_SEND)
        return shareLinkIntent
    }

    private fun provideShareLinkChooser(): Intent {
        return Intent.createChooser(shareLinkIntent, null)
    }

    private fun provideSupportEmailIntent(): Intent {
        return Intent(Intent.ACTION_SENDTO)
    }

    private fun provideTermsIntent(): Intent {
        return Intent(Intent.ACTION_VIEW)
    }

    //mediaPlayer===================================================================================
    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository())
    }

    private fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(provideMediaPlayer())
    }

    private fun provideMediaPlayer(): MediaPlayer{
        return MediaPlayer()
    }

}
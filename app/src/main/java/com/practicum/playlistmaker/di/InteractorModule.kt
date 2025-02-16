package com.practicum.playlistmaker.di

import android.util.Log
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.api.GetPlayerIntentUseCase
import com.practicum.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.impl.HistoryTracksInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SearchTracksInteractor> {
        Log.d("wtf", "searchTracksInteractor created")
        SearchTracksInteractorImpl(get())
    }

    single<HistoryTracksInteractor> {
        Log.d("wtf", "historyTracksInteractor created")
        HistoryTracksInteractorImpl(get())
    }

    single {
        Log.d("wtf", "getPlayerIntentUseCase created")
        GetPlayerIntentUseCase(get())
    }

    single<SettingsInteractor> {
        Log.d("wtf", "settingsInteractor created")
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        Log.d("wtf", "sharingInteractor created")
        SharingInteractorImpl(get(), get())
    }

    single<MediaPlayerInteractor> {
        Log.d("wtf", "playerInteractor created")
        MediaPlayerInteractorImpl(get())
    }

}
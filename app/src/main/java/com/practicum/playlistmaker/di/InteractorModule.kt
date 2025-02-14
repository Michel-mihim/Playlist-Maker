package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.domain.api.GetPlayerIntentUseCase
import com.practicum.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.impl.HistoryTracksInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }

    single<HistoryTracksInteractor> {
        HistoryTracksInteractorImpl(get())
    }

    single {
        GetPlayerIntentUseCase(get())
    }
}
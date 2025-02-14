package com.practicum.playlistmaker.di

import android.content.Intent
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.data.impl.HistoryTracksRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.PlayerIntentGetterImpl
import com.practicum.playlistmaker.search.data.impl.SearchTracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryTracksRepository
import com.practicum.playlistmaker.search.domain.api.PlayerIntentGetter
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchTracksRepository> {
        SearchTracksRepositoryImpl(get())
    }

    single<HistoryTracksRepository> {
        HistoryTracksRepositoryImpl(get())
    }

    single<PlayerIntentGetter> {
        PlayerIntentGetterImpl(Intent(androidContext(), PlayerActivity::class.java), get())
    }
}
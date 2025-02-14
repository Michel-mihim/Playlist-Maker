package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.data.impl.SearchTracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchTracksRepository> {
        SearchTracksRepositoryImpl(get())
    }
}
package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.impl.SearchTracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.ITunesApiService
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApiService> {
        val iTunesBaseUrl = "https://itunes.apple.com"
        Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<SearchTracksRepository> {
        SearchTracksRepositoryImpl(get())
    }

    single<SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }

}
package com.practicum.playlistmaker.di

import android.content.Intent
import android.util.Log
import com.practicum.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.data.impl.HistoryTracksRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.PlayerIntentGetterImpl
import com.practicum.playlistmaker.search.data.impl.SearchTracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryTracksRepository
import com.practicum.playlistmaker.search.domain.api.PlayerIntentGetter
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.data.impl.TextResourseGetterImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.TextResourseGetter
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

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), androidContext())
    }

    factory<ExternalNavigator> {
        val shareLinkIntent = Intent(Intent.ACTION_SEND)
        ExternalNavigatorImpl(
            shareLinkIntent,
            Intent.createChooser(shareLinkIntent, null),
            Intent(Intent.ACTION_SENDTO),
            Intent(Intent.ACTION_VIEW)
        )
    }

    factory<TextResourseGetter> {
        TextResourseGetterImpl(androidContext())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

}
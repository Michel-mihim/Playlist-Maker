package com.practicum.playlistmaker.di

import android.app.Application
import android.util.Log
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get(), androidApplication())
    }

    viewModel {
        PlayerViewModel(get())
    }

}
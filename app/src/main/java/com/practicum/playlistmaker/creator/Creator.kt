package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import com.practicum.playlistmaker.search.data.impl.HistoryTracksRepositoryImpl
import com.practicum.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryTracksRepository
import com.practicum.playlistmaker.search.domain.impl.HistoryTracksInteractorImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.api.PlayerIntentGetter
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import com.practicum.playlistmaker.search.data.impl.PlayerIntentGetterImpl
import com.practicum.playlistmaker.search.domain.api.GetPlayerIntentUseCase
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.api.TextResourseGetter
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.practicum.playlistmaker.sharing.data.impl.TextResourseGetterImpl
import com.practicum.playlistmaker.utils.constants.Constants

object Creator {

    //searchTracks==================================================================================


    //history=======================================================================================


    //settings======================================================================================


    //playerIntentGetter============================================================================


    //sharing=======================================================================================
    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            provideExternalNavigator(),
            provideTextResourseGetter(context)
        )
    }

    private fun provideTextResourseGetter(context: Context): TextResourseGetter {
        return TextResourseGetterImpl(context)
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


}
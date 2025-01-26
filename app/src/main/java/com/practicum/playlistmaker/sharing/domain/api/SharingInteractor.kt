package com.practicum.playlistmaker.sharing.domain.api

import android.content.Intent


interface SharingInteractor {
    fun shareApp(onChooserReady: (Any) -> Unit)
    fun openTerms()
    fun openSupport()
}
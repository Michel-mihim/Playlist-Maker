package com.practicum.playlistmaker.sharing.domain.api


interface SharingInteractor {
    fun shareApp(onChooserReady: (Any) -> Unit)
    fun openTerms(onTermsIntentReady: (Any) -> Unit)
    fun openSupport(onSupportEmailIntentReady: (Any) -> Unit)
}
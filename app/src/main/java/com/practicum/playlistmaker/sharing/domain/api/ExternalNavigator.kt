package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String, onChooserReady: (Any) -> Unit)
    fun openLink(link: String, onTermsIntentReady: (Any) -> Unit)
    fun openEmail(email: EmailData, onSupportEmailIntentReady: (Any) -> Unit)
}
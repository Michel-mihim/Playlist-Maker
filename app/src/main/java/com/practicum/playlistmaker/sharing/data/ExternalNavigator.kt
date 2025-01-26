package com.practicum.playlistmaker.sharing.data

import android.content.Intent
import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String, onChooserReady: (Any) -> Unit)
    fun openLink(link: String)
    fun openEmail(email: EmailData)
}
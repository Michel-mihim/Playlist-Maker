package com.practicum.playlistmaker.sharing.data.impl

import android.content.Intent
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(
    private val shareIntent: Intent,
    private val chooser: Intent
) : ExternalNavigator {

    override fun shareLink(
        text: String,
        onChooserReady: ((Any) -> Unit)
    ) {
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Полезная ссылка")
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        onChooserReady.invoke(chooser)
    }

    override fun openLink(link: String) {

    }

    override fun openEmail(email: EmailData) {

    }

}
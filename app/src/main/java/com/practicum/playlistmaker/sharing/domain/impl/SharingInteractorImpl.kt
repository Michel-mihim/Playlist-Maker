package com.practicum.playlistmaker.sharing.domain.impl

import android.content.Intent
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp(onChooserReady: (Any) -> Unit) {
        externalNavigator.shareLink(getShareAppLink(), onChooserReady)
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return "https://practicum.yandex.ru/profile/android-developer/"
    }

    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/"
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            "ya-mihim@yandex.ru",
            "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            "Спасибо разработчикам и разработчицам за крутое приложение!"
        )
    }

}
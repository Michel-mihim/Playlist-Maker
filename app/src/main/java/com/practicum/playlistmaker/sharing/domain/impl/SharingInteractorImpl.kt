package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.api.TextResourseGetter
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val textResourseGetter: TextResourseGetter
) : SharingInteractor {
    override fun shareApp(onChooserReady: (Any) -> Unit) {
        externalNavigator.shareLink(getShareAppLink(), onChooserReady)
    }

    override fun openTerms(onTermsIntentReady: (Any) -> Unit) {
        externalNavigator.openLink(getTermsLink(), onTermsIntentReady)
    }

    override fun openSupport(onSupportEmailIntentReady: (Any) -> Unit) {
        externalNavigator.openEmail(getSupportEmailData(), onSupportEmailIntentReady)
    }

    private fun getShareAppLink(): String {
        return textResourseGetter.getShareAppLink()
    }

    private fun getTermsLink(): String {
        return textResourseGetter.getTermsLink()
    }

    private fun getSupportEmailData(): EmailData {
        return textResourseGetter.getSupportEmailData()
    }

}
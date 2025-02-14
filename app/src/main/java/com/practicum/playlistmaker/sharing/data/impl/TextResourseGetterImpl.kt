package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.TextResourseGetter
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class TextResourseGetterImpl(
    val context: Context
): TextResourseGetter {
    override fun getTermsLink(): String {
        return getString(context, R.string.license_url)
    }

    override fun getShareAppLink(): String {
        return getString(context, R.string.share_message)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            getString(context, R.string.student_email),
            getString(context, R.string.letter_text_subject),
            getString(context, R.string.letter_text)
        )
    }
}